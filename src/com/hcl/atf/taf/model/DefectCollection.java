package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hcl.atf.taf.mongodb.model.DefectCollectionMongo;

@Entity
@Table(name = "defect_collection")
public class DefectCollection implements Cloneable {
	private static final Log log = LogFactory.getLog(DefectCollection.class);

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "_id", unique = true, nullable = false)
	private Integer _id;
	
	@Column(name = "_class")
	private String _class;
	
	@Column(name = "defectId")
	private String defectId;
	
	@Column(name = "title")
	private String title;
	
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
	
	@Column(name = "competencyManager")
	private String competencyManager;
	
	@Column(name = "versionName")
	private String versionName;
	
	@Column(name = "versionId")
	private Integer versionId;
	
	@Column(name = "buildName")
	private String buildName;
	
	@Column(name = "buildId")
	private Integer buildId;
	
	@Column(name = "workpackageName")
	private String workpackageName;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "releases")
	private String release;
	
	@Column(name = "requestType")
	private String requestType;
	
	@Column(name = "priority")
	private String priority;
	
	@Column(name = "resolution")
	private String resolution;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "severity")
	private String severity;
	
	@Column(name = "detection")
	private String detection;
	
	@Column(name = "injection")
	private String injection;
	
	@Column(name = "internalDefect")
	private String internalDefect;
	
	@Column(name = "feature")
	private String feature;
	
	@Column(name = "raisedDate")
	private Date raisedDate;
	
	@Column(name = "assignedDate")
	private Date assignedDate;
	
	@Column(name = "kbid")
	private String kbid;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "createdDate")
	private Date createdDate;
	
	@Column(name = "updatedDate")
	private Date updatedDate;
	
	@Column(name = "expectedEffort")
	private Float expectedEffort;
	
	@Column(name = "plannedClosureDate")
	private Date plannedClosureDate;
	
	@Column(name = "closureDate")
	private Date closureDate;
	
	@Column(name = "updatedBy")
	private String updatedBy;
	
	@Column(name = "raisedBy")
	private String raisedBy;
	
	@Column(name = "owner")
	private String owner;
	
	@Column(name = "assignedTo")
	private String assignedTo;
	
	@Column(name = "lastUpdatedDate")
	private Date lastUpdatedDate;
	
	@Column(name = "file")
	private String file;
	
	@Column(name = "primaryFeature")
	private String primaryFeature;
	
	@Column(name = "primaryFeatureParent")
	private String primaryFeatureParent;
	
	@Column(name = "secondaryFeature")
	private String secondaryFeature;
	
	@Column(name = "mapped")
	private String mapped;
	
	@Column(name = "testCases")
	private String testCases;
	
	@Column(name = "confirmed")
	private String confirmed;
	
	@Column(name = "testExecutionResultBugId")
	private Integer testExecutionResultBugId;
	
	@Column(name = "testCaseExecutionResult")
	private Integer testCaseExecutionResult;
	
	@Column(name = "environment")
	private String environment;
	
	@Column(name = "closedDate")
	private Date closedDate;
	
	@Column(name = "resolvedDate")
	private Date resolvedDate;
	
	@Column(name = "verifiedDate")
	private Date verifiedDate;
	
	@Column(name = "parentState")
	private String parentState;
	
	@Column(name = "driverState")
	private String driverState;
	
	@Column(name = "specificationState")
	private String specificationState;
	
	@Column(name = "closedStatus")
	private String closedStatus;
	
	@Column(name = "resolvedInfo")
	private String resolvedInfo;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "activityName")
	private String activityName;
	
	@Column(name = "activityComponent")
	private String activityComponent;

	public DefectCollection() {
		// this._id = "";
		this._class = "";
		this.defectId = "";
		this.title = "";
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
		this.workpackageName = "";
		this.description = "";
		this.release = "";
		this.requestType = "";
		this.priority = "";
		this.resolution = "";
		this.category = "";
		this.severity = "";
		this.detection = "";
		this.injection = "";
		this.internalDefect = "";
		this.feature = "";
		this.raisedDate = null;
		this.assignedDate = null;
		this.kbid = "";
		this.status = "";
		this.createdDate = null;
		this.updatedDate = null;
		this.expectedEffort = 0.0F;
		this.plannedClosureDate = null;
		this.closureDate = null;
		this.updatedBy = "";
		this.raisedBy = "";
		this.owner = "";
		this.assignedTo = "";
		this.lastUpdatedDate = null;
		this.file = "";
		this.primaryFeature = "";
		this.primaryFeatureParent = "";
		this.secondaryFeature = "";
		this.mapped = "";
		this.testCases = "";
		this.confirmed = "";
		this.testExecutionResultBugId = 0;
		this.testCaseExecutionResult = 0;
		this.environment = "";
		this.closedDate = null;
		this.resolvedDate = null;
		this.verifiedDate = null;
		this.parentState = "";
		this.driverState = "";
		this.specificationState = "";
		this.closedStatus = "";
		this.resolvedInfo = "";
		this.remarks = "";
		this.type = "";
		this.activityName = "";
		this.activityComponent = "";
	}

	public DefectCollection(DefectCollectionMongo defectCollectionMongo) {
		this._id = defectCollectionMongo.getId();
		this._class = defectCollectionMongo.get_class();
		this.defectId = defectCollectionMongo.getDefectId();
		this.title = defectCollectionMongo.getTitle();
		this.testCentersName = defectCollectionMongo.getTestCentersName();
		this.testCentersId = defectCollectionMongo.getTestCentersId();
		this.testFactoryName = defectCollectionMongo.getTestFactoryName();
		this.testFactoryId = defectCollectionMongo.getTestFactoryId();
		this.customerName = defectCollectionMongo.getCustomerName();
		this.customerId = defectCollectionMongo.getCustomerId();
		this.productName = defectCollectionMongo.getProductName();
		this.productId = defectCollectionMongo.getProductId();
		this.productManager = defectCollectionMongo.getProductManager();
		this.project = defectCollectionMongo.getProject();
		this.projectId = defectCollectionMongo.getProjectId();
		this.projectManager = defectCollectionMongo.getProjectManager();
		this.competency = defectCollectionMongo.getCompetency();
		this.competencyId = defectCollectionMongo.getCompetencyId();
		this.competencyManager = defectCollectionMongo.getCompetencyManager();
		this.versionName = defectCollectionMongo.getVersionName();
		this.versionId = defectCollectionMongo.getVersionId();
		this.buildName = defectCollectionMongo.getBuildName();
		this.buildId = defectCollectionMongo.getBuildId();
		this.workpackageName = defectCollectionMongo.getWorkpackageName();
		this.description = defectCollectionMongo.getDescription();
		this.release = defectCollectionMongo.getRelease();
		this.requestType = defectCollectionMongo.getRequestType();
		this.priority = defectCollectionMongo.getPriority();
		this.resolution = defectCollectionMongo.getResolution();
		this.category = defectCollectionMongo.getCategory();
		this.severity = defectCollectionMongo.getSeverity();
		this.detection = defectCollectionMongo.getDetection();
		this.injection = defectCollectionMongo.getInjection();
		this.internalDefect = defectCollectionMongo.getInternalDefect();
		this.feature = defectCollectionMongo.getFeature();
		this.raisedDate = (Date) defectCollectionMongo.getRaisedDate();
		this.assignedDate = (Date) defectCollectionMongo.getAssignedDate();
		this.kbid = defectCollectionMongo.getKbid();
		this.status = defectCollectionMongo.getStatus();
		this.createdDate = (Date) defectCollectionMongo.getCreatedDate();
		this.updatedDate = (Date) defectCollectionMongo.getUpdatedDate();
		this.expectedEffort = defectCollectionMongo.getExpectedEffort();
		this.plannedClosureDate = (Date) defectCollectionMongo.getPlannedClosureDate();
		this.closureDate = (Date) defectCollectionMongo.getClosureDate();
		this.updatedBy = defectCollectionMongo.getUpdatedBy();
		this.raisedBy = defectCollectionMongo.getRaisedBy();
		this.owner = defectCollectionMongo.getOwner();
		this.assignedTo = defectCollectionMongo.getAssignedTo();
		this.lastUpdatedDate = (Date) defectCollectionMongo.getLastUpdatedDate();
		this.file = defectCollectionMongo.getFile();
		this.primaryFeature = defectCollectionMongo.getPrimaryFeature();
		this.primaryFeatureParent = defectCollectionMongo.getPrimaryFeatureParent();
		this.secondaryFeature = defectCollectionMongo.getSecondaryFeature();
		this.mapped = defectCollectionMongo.getMapped();
		this.testCases = defectCollectionMongo.getTestCases();
		this.confirmed = defectCollectionMongo.getConfirmed();
		this.testExecutionResultBugId = defectCollectionMongo.getTestExecutionResultBugId();
		this.testCaseExecutionResult = defectCollectionMongo.getTestCaseExecutionResult();
		this.environment = defectCollectionMongo.getEnvironment();
		this.closedDate = (Date) defectCollectionMongo.getClosedDate();
		this.resolvedDate = (Date) defectCollectionMongo.getResolvedDate();
		this.verifiedDate = (Date) defectCollectionMongo.getVerifiedDate();
		this.parentState = defectCollectionMongo.getParentState();
		this.driverState = defectCollectionMongo.getDriverState();
		this.specificationState = defectCollectionMongo.getSpecificationState();
		this.closedStatus = defectCollectionMongo.getClosedStatus();
		this.resolvedInfo = defectCollectionMongo.getResolvedInfo();
		this.remarks = defectCollectionMongo.getRemarks();
		this.type = defectCollectionMongo.getType();
		this.activityName = defectCollectionMongo.getActivityName();
		this.activityComponent = defectCollectionMongo.getActivityComponent();
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

	public String getDefectId() {
		return defectId;
	}

	public void setDefectId(String defectId) {
		this.defectId = defectId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getWorkpackageName() {
		return workpackageName;
	}

	public void setWorkpackageName(String workpackageName) {
		this.workpackageName = workpackageName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRelease() {
		return release;
	}

	public void setRelease(String release) {
		this.release = release;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getDetection() {
		return detection;
	}

	public void setDetection(String detection) {
		this.detection = detection;
	}

	public String getInjection() {
		return injection;
	}

	public void setInjection(String injection) {
		this.injection = injection;
	}

	public String getInternalDefect() {
		return internalDefect;
	}

	public void setInternalDefect(String internalDefect) {
		this.internalDefect = internalDefect;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public Date getRaisedDate() {
		return raisedDate;
	}

	public void setRaisedDate(Date raisedDate) {
		this.raisedDate = raisedDate;
	}

	public Date getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(Date assignedDate) {
		this.assignedDate = assignedDate;
	}

	public String getKbid() {
		return kbid;
	}

	public void setKbid(String kbid) {
		this.kbid = kbid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Float getExpectedEffort() {
		return expectedEffort;
	}

	public void setExpectedEffort(Float expectedEffort) {
		this.expectedEffort = expectedEffort;
	}

	public Date getPlannedClosureDate() {
		return plannedClosureDate;
	}

	public void setPlannedClosureDate(Date plannedClosureDate) {
		this.plannedClosureDate = plannedClosureDate;
	}

	public Date getClosureDate() {
		return closureDate;
	}

	public void setClosureDate(Date closureDate) {
		this.closureDate = closureDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getRaisedBy() {
		return raisedBy;
	}

	public void setRaisedBy(String raisedBy) {
		this.raisedBy = raisedBy;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getPrimaryFeature() {
		return primaryFeature;
	}

	public void setPrimaryFeature(String primaryFeature) {
		this.primaryFeature = primaryFeature;
	}

	public String getPrimaryFeatureParent() {
		return primaryFeatureParent;
	}

	public void setPrimaryFeatureParent(String primaryFeatureParent) {
		this.primaryFeatureParent = primaryFeatureParent;
	}

	public String getSecondaryFeature() {
		return secondaryFeature;
	}

	public void setSecondaryFeature(String secondaryFeature) {
		this.secondaryFeature = secondaryFeature;
	}

	public String getMapped() {
		return mapped;
	}

	public void setMapped(String mapped) {
		this.mapped = mapped;
	}

	public String getTestCases() {
		return testCases;
	}

	public void setTestCases(String testCases) {
		this.testCases = testCases;
	}

	public String getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(String confirmed) {
		this.confirmed = confirmed;
	}

	public Integer getTestExecutionResultBugId() {
		return testExecutionResultBugId;
	}

	public void setTestExecutionResultBugId(Integer testExecutionResultBugId) {
		this.testExecutionResultBugId = testExecutionResultBugId;
	}

	public Integer getTestCaseExecutionResult() {
		return testCaseExecutionResult;
	}

	public void setTestCaseExecutionResult(Integer testCaseExecutionResult) {
		this.testCaseExecutionResult = testCaseExecutionResult;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public Date getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}

	public Date getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(Date resolvedDate) {
		this.resolvedDate = resolvedDate;
	}

	public Date getVerifiedDate() {
		return verifiedDate;
	}

	public void setVerifiedDate(Date verifiedDate) {
		this.verifiedDate = verifiedDate;
	}

	public String getParentState() {
		return parentState;
	}

	public void setParentState(String parentState) {
		this.parentState = parentState;
	}

	public String getDriverState() {
		return driverState;
	}

	public void setDriverState(String driverState) {
		this.driverState = driverState;
	}

	public String getSpecificationState() {
		return specificationState;
	}

	public void setSpecificationState(String specificationState) {
		this.specificationState = specificationState;
	}

	public String getClosedStatus() {
		return closedStatus;
	}

	public void setClosedStatus(String closedStatus) {
		this.closedStatus = closedStatus;
	}

	public String getResolvedInfo() {
		return resolvedInfo;
	}

	public void setResolvedInfo(String resolvedInfo) {
		this.resolvedInfo = resolvedInfo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityComponent() {
		return activityComponent;
	}

	public void setActivityComponent(String activityComponent) {
		this.activityComponent = activityComponent;
	}

	
	@Override
    public Object clone() {
        try {
            return super.clone();
        }
        catch (Exception e) {
            log.error("ERROR  ",e);
        }
        return null;
    }
}
