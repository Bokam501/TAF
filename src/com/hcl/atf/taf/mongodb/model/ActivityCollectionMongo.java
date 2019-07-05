package com.hcl.atf.taf.mongodb.model;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ActivityCollection;

@Document(collection = "activity_collection")
public class ActivityCollectionMongo {
	private static final Log log = LogFactory.getLog(ActivityCollectionMongo.class);
	@Transient
	private SimpleDateFormat dateFormatForMongoDB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

	@Id
	private Integer id;
	private String _class;
	private String testCentersName;
	private Integer testCentersId;
	private String testFactoryName;
	private Integer testFactoryId;
	private String customerName;
	private Integer customerId;
	private String productName;
	private Integer productId;
	private String productManager;
	private String project;
	private Integer projectId;
	private String projectManager;
	private String competency;
	private Integer competencyId;
	private String competencyManager;
	private String versionName;
	private Integer versionId;
	private String buildName;
	private Integer buildId;
	private Object weekDate;
	private Object createdDate;
	private Object updateDate;
	private String phase;
	private String workpackageName;
	private String activityBatchNo;
	private String activityType;
	private String activityCategory;
	private String activityName;
	private Float activitySizeActual;
	private Float activitySizePlanned;
	private String activityReference;
	private String activityTracker;
	private String activityEnvironmentPrimary;
	private String activityEnvironmentSecondary;
	private String activityEnvironmentTertiary;
	private String weightageType;
	private Float weightageUnit;
	private Float workUnitPlanned;
	private Float workUnitActual;
	private String activitySeverity;
	private String activityResolution;
	private String activityStatus;
	private String activitySource;
	private String activityComponent;
	private String activityStage;
	private Object actualActivityStartDate;
	private Object actualActivityEndDate;
	private Object plannedActivityStartDate;
	private Object plannedActivityEndDate;
	private Object revisedActivityStartDate;
	private Object revisedActivityEndDate;
	private Float actualActivityEffort;
	private Float plannedActivityEffort;
	private Float revisedActivityEffort;
	private String activityTag;
	private Float cumulativeActivityActual;
	private Float cumulativeActivityPlanned;
	private String activityRaisedBy;
	private String activityOwner;
	private String activityAssignedTo;
	private String activityReviewer1;
	private String activityReviewer2;
	private String activityReviewer3;
	private String activityReviewer4;
	private String activityReviewer5;
	private String activityParent;
	private Float activityExecutionEffort;
	private Float activityReviewEffort1;
	private Float activityReviewEffort2;
	private Float activityReviewEffort3;
	private Float activityReviewEffort4;
	private Float activityReviewEffort5;
	private Float activityReworkEffort;
	private Float l0ResourceCount;
	private Float l1ResourceCount;
	private Float l2ResourceCount;
	private Float l3ResourceCount;
	private Float l4ResourceCount;
	private Float l5ResourceCount;
	private Float l6ResourceCount;
	private Float l7ResourceCount;
	private String remarks;
	private Float eVa;
	private Float eVb;
	private Float sVa;
	private Float sVb;
	private String type;
	
	private Object activityEndDate;
	private String status;

	public ActivityCollectionMongo(){
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
		this.versionName = "";
		this.versionId = 0;
		this.buildName = "";
		this.buildId = 0;
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
		
		this.activityEndDate=null;
		this.status="";
	}

	public ActivityCollectionMongo(ActivityCollection activityCollection){
		this.id = activityCollection.get_id();
		this._class = this.getClass().getName().replace("\\", ".").replace("/", ".");
		this.testCentersName = activityCollection.getTestCentersName();
		this.testCentersId = activityCollection.getTestCentersId();
		this.testFactoryName = activityCollection.getTestFactoryName();
		this.testFactoryId = activityCollection.getTestFactoryId();
		this.customerName = activityCollection.getCustomerName();
		this.customerId = activityCollection.getCustomerId();
		this.productName = activityCollection.getProductName();
		this.productId = activityCollection.getProductId();
		this.productManager = activityCollection.getProductManager();
		this.project = activityCollection.getProject();
		this.projectId = activityCollection.getProjectId();
		this.projectManager = activityCollection.getProjectManager();
		this.competency = activityCollection.getCompetency();
		this.competencyId = activityCollection.getCompetencyId();
		this.competencyManager = activityCollection.getCompetencyManager();
		this.versionName = activityCollection.getVersionName();
		this.versionId = activityCollection.getVersionId();
		this.buildName = activityCollection.getBuildName();
		this.buildId = activityCollection.getBuildId();
		this.weekDate = setDateForMongoDB(activityCollection.getWeekDate());
		this.createdDate = setDateForMongoDB(activityCollection.getCreatedDate());
		this.updateDate = setDateForMongoDB(activityCollection.getUpdateDate());
		this.phase = activityCollection.getPhase();
		this.workpackageName = activityCollection.getWorkpackageName();
		this.activityBatchNo = activityCollection.getActivityBatchNo();
		this.activityType = activityCollection.getActivityType();
		this.activityCategory = activityCollection.getActivityCategory();
		this.activityName = activityCollection.getActivityName();
		this.activitySizeActual = activityCollection.getActivitySizeActual();
		this.activitySizePlanned = activityCollection.getActivitySizePlanned();
		this.activityReference = activityCollection.getActivityReference();
		this.activityTracker = activityCollection.getActivityTracker();
		this.activityEnvironmentPrimary = activityCollection.getActivityEnvironmentPrimary();
		this.activityEnvironmentSecondary = activityCollection.getActivityEnvironmentSecondary();
		this.activityEnvironmentTertiary = activityCollection.getActivityEnvironmentTertiary();
		this.weightageType = activityCollection.getWeightageType();
		this.weightageUnit = activityCollection.getWeightageUnit();
		this.workUnitPlanned = activityCollection.getWorkUnitPlanned();
		this.workUnitActual = activityCollection.getWorkUnitActual();
		this.activitySeverity = activityCollection.getActivitySeverity();
		this.activityResolution = activityCollection.getActivityResolution();
		this.activityStatus = activityCollection.getActivityStatus();
		this.activitySource = activityCollection.getActivitySource();
		this.activityComponent = activityCollection.getActivityComponent();
		this.activityStage = activityCollection.getActivityStage();
		this.actualActivityStartDate = setDateForMongoDB(activityCollection.getActualActivityStartDate());
		this.actualActivityEndDate = setDateForMongoDB(activityCollection.getActualActivityEndDate());
		this.plannedActivityStartDate = setDateForMongoDB(activityCollection.getPlannedActivityStartDate());
		this.plannedActivityEndDate = setDateForMongoDB(activityCollection.getPlannedActivityEndDate());
		this.revisedActivityStartDate = setDateForMongoDB(activityCollection.getRevisedActivityStartDate());
		this.revisedActivityEndDate = setDateForMongoDB(activityCollection.getRevisedActivityEndDate());
		this.actualActivityEffort = activityCollection.getActualActivityEffort();
		this.plannedActivityEffort = activityCollection.getPlannedActivityEffort();
		this.revisedActivityEffort = activityCollection.getRevisedActivityEffort();
		this.activityTag = activityCollection.getActivityTag();
		this.cumulativeActivityActual = activityCollection.getCumulativeActivityActual();
		this.cumulativeActivityPlanned = activityCollection.getCumulativeActivityPlanned();
		this.activityRaisedBy = activityCollection.getActivityRaisedBy();
		this.activityOwner = activityCollection.getActivityOwner();
		this.activityAssignedTo = activityCollection.getActivityAssignedTo();
		this.activityReviewer1 = activityCollection.getActivityReviewer1();
		this.activityReviewer2 = activityCollection.getActivityReviewer2();
		this.activityReviewer3 = activityCollection.getActivityReviewer3();
		this.activityReviewer4 = activityCollection.getActivityReviewer4();
		this.activityReviewer5 = activityCollection.getActivityReviewer5();	
		this.activityParent = activityCollection.getActivityParent();
		this.activityExecutionEffort = activityCollection.getActivityExecutionEffort();
		this.activityReviewEffort1 = activityCollection.getActivityReviewEffort1();		
		this.activityReviewEffort2 = activityCollection.getActivityReviewEffort2();
		this.activityReviewEffort3 = activityCollection.getActivityReviewEffort3();
		this.activityReviewEffort4 = activityCollection.getActivityReviewEffort4();
		this.activityReviewEffort5 = activityCollection.getActivityReviewEffort5();
		this.activityReworkEffort = activityCollection.getActivityReworkEffort();
		this.l0ResourceCount = activityCollection.getL0ResourceCount();
		this.l1ResourceCount = activityCollection.getL1ResourceCount();
		this.l2ResourceCount = activityCollection.getL2ResourceCount();
		this.l3ResourceCount = activityCollection.getL3ResourceCount();
		this.l4ResourceCount = activityCollection.getL4ResourceCount();
		this.l5ResourceCount = activityCollection.getL5ResourceCount();
		this.l6ResourceCount = activityCollection.getL6ResourceCount();
		this.l7ResourceCount = activityCollection.getL7ResourceCount();
		this.remarks = activityCollection.getRemarks();
		this.eVa = activityCollection.geteVa();
		this.eVb = activityCollection.geteVb();
		this.sVa = activityCollection.getsVa();
		this.sVb = activityCollection.getsVb();
		this.type = activityCollection.getType();
		
		if(activityCollection.getActualActivityEndDate()!=null){
			this.activityEndDate=setDateForMongoDB(activityCollection.getActualActivityEndDate());
			this.status = "Actual";
		}else if(activityCollection.getRevisedActivityEndDate()!=null){
			this.activityEndDate=setDateForMongoDB(activityCollection.getRevisedActivityEndDate());
			this.status = "Planned";
		}else{
			this.activityEndDate=setDateForMongoDB(activityCollection.getPlannedActivityEndDate());
			this.status = "Planned";
		}
		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Object _id) {
		String formatCheck = _id.toString();
		if(formatCheck.contains("oid=")){
			_id = formatCheck.split("oid=")[1].split("}")[0];
		}
		this.id = Integer.parseInt(_id.toString());
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

	public Object getWeekDate() {
		return weekDate;
	}

	public void setWeekDate(Object weekDate) {
		String formatCheck = weekDate.toString();
		if(formatCheck.contains("date=")){
			try {
				weekDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.weekDate = weekDate;
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
	
	public Object getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Object updateDate) {
		String formatCheck = updateDate.toString();
		if(formatCheck.contains("date=")){
			try {
				updateDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
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

	public Object getActualActivityStartDate() {
		return actualActivityStartDate;
	}

	public void setActualActivityStartDate(Object actualActivityStartDate) {
		String formatCheck = actualActivityStartDate.toString();
		if(formatCheck.contains("date=")){
			try {
				actualActivityStartDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.actualActivityStartDate = actualActivityStartDate;
	}

	public Object getActualActivityEndDate() {
		return actualActivityEndDate;
	}

	public void setActualActivityEndDate(Object actualActivityEndDate) {
		String formatCheck = actualActivityEndDate.toString();
		if(formatCheck.contains("date=")){
			try {
				actualActivityEndDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.actualActivityEndDate = actualActivityEndDate;
	}

	public Object getPlannedActivityStartDate() {
		return plannedActivityStartDate;
	}

	public void setPlannedActivityStartDate(Object plannedActivityStartDate) {
		String formatCheck = plannedActivityStartDate.toString();
		if(formatCheck.contains("date=")){
			try {
				plannedActivityStartDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.plannedActivityStartDate = plannedActivityStartDate;
	}

	public Object getPlannedActivityEndDate() {
		return plannedActivityEndDate;
	}

	public void setPlannedActivityEndDate(Object plannedActivityEndDate) {
		String formatCheck = plannedActivityEndDate.toString();
		if(formatCheck.contains("date=")){
			try {
				plannedActivityEndDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.plannedActivityEndDate = plannedActivityEndDate;
	}

	public Object getRevisedActivityStartDate() {
		return revisedActivityStartDate;
	}

	public void setRevisedActivityStartDate(Object revisedActivityStartDate) {
		String formatCheck = revisedActivityStartDate.toString();
		if(formatCheck.contains("date=")){
			try {
				revisedActivityStartDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.revisedActivityStartDate = revisedActivityStartDate;
	}

	public Object getRevisedActivityEndDate() {
		return revisedActivityEndDate;
	}

	public void setRevisedActivityEndDate(Object revisedActivityEndDate) {
		String formatCheck = revisedActivityEndDate.toString();
		if(formatCheck.contains("date=")){
			try {
				revisedActivityEndDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
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

	public SimpleDateFormat getDateFormatForMongoDB() {
		return dateFormatForMongoDB;
	}

	public void setDateFormatForMongoDB(SimpleDateFormat dateFormatForMongoDB) {
		this.dateFormatForMongoDB = dateFormatForMongoDB;
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

	public Float getL0ResourceCount() {
		return l0ResourceCount;
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
	

	
	public Object getActivityEndDate() {
		return activityEndDate;
	}

	public void setActivityEndDate(Object activityEndDate) {
		String formatCheck = activityEndDate.toString();
		if(formatCheck.contains("date=")){
			try {
				activityEndDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.activityEndDate = activityEndDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	private Date setDateForMongoDB(Date dateToMongoDB){
		if(dateToMongoDB != null){
			Calendar dateToMongoDBCalendar = Calendar.getInstance();
			dateToMongoDBCalendar.setTime(dateToMongoDB);
			dateToMongoDBCalendar.add(Calendar.MILLISECOND, dateToMongoDBCalendar.getTimeZone().getRawOffset());
			dateToMongoDB = dateToMongoDBCalendar.getTime();
		}
		return dateToMongoDB;
	}

	
}
