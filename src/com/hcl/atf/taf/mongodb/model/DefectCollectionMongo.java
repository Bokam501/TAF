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

import com.hcl.atf.taf.model.DefectCollection;

@Document(collection = "defect_collection")
public class DefectCollectionMongo {
	private static final Log log = LogFactory.getLog(DefectCollectionMongo.class);
	@Transient
	private SimpleDateFormat dateFormatForMongoDB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
	
	@Id
	private Integer id;
	private String _class;
	private String defectId;
	private String title;
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
	private String workpackageName;
	private String description;
	private String release;
	private String requestType;
	private String priority;
	private String resolution;
	private String category;
	private String severity;
	private String detection;
	private String injection;
	private String internalDefect;
	private String feature;
	private Object raisedDate;
	private Object assignedDate;
	private String kbid;
	private String status;
	private Object createdDate;
	private Object updatedDate;
	private Float expectedEffort;
	private Object plannedClosureDate;
	private Object closureDate;
	private String updatedBy;
	private String raisedBy;
	private String owner;
	private String assignedTo;
	private Object lastUpdatedDate;
	private String file;
	private String primaryFeature;
	private String primaryFeatureParent;
	private String secondaryFeature;
	private String mapped;
	private String testCases;
	private String confirmed;
	private Integer testExecutionResultBugId;
	private Integer testCaseExecutionResult;
	private String environment;
	private Object closedDate;
	private Object resolvedDate;
	private Object verifiedDate;
	private String parentState;
	private String driverState;
	private String specificationState;
	private String closedStatus;
	private String resolvedInfo;
	private String remarks;
	private String type;
	private String activityName;
	private String activityComponent;
	
	public DefectCollectionMongo() {
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

	public DefectCollectionMongo(DefectCollection defectCollection) {
		this.id = defectCollection.get_id();
		this._class = defectCollection.get_class();
		this.defectId = defectCollection.getDefectId();
		this.title = defectCollection.getTitle();
		this.testCentersName = defectCollection.getTestCentersName();
		this.testCentersId = defectCollection.getTestCentersId();
		this.testFactoryName = defectCollection.getTestFactoryName();
		this.testFactoryId = defectCollection.getTestFactoryId();
		this.customerName = defectCollection.getCustomerName();
		this.customerId = defectCollection.getCustomerId();
		this.productName = defectCollection.getProductName();
		this.productId = defectCollection.getProductId();
		this.productManager = defectCollection.getProductManager();
		this.project = defectCollection.getProject();
		this.projectId = defectCollection.getProjectId();
		this.projectManager = defectCollection.getProjectManager();
		this.competency = defectCollection.getCompetency();
		this.competencyId = defectCollection.getCompetencyId();
		this.competencyManager = defectCollection.getCompetencyManager();
		this.versionName = defectCollection.getVersionName();
		this.versionId = defectCollection.getVersionId();
		this.buildName = defectCollection.getBuildName();
		this.buildId = defectCollection.getBuildId();
		this.workpackageName = defectCollection.getWorkpackageName();
		this.description = defectCollection.getDescription();
		this.release = defectCollection.getRelease();
		this.requestType = defectCollection.getRequestType();
		this.priority = defectCollection.getPriority();
		this.resolution = defectCollection.getResolution();
		this.category = defectCollection.getCategory();
		this.severity = defectCollection.getSeverity();
		this.detection = defectCollection.getDetection();
		this.injection = defectCollection.getInjection();
		this.internalDefect = defectCollection.getInternalDefect();
		this.feature = defectCollection.getFeature();
		this.raisedDate = setDateForMongoDB(defectCollection.getRaisedDate());
		this.assignedDate = setDateForMongoDB(defectCollection.getAssignedDate());
		this.kbid = defectCollection.getKbid();
		this.status = defectCollection.getStatus();
		this.createdDate = setDateForMongoDB(defectCollection.getCreatedDate());
		this.updatedDate = setDateForMongoDB(defectCollection.getUpdatedDate());
		this.expectedEffort = defectCollection.getExpectedEffort();
		this.plannedClosureDate = setDateForMongoDB(defectCollection.getPlannedClosureDate());
		this.closureDate = setDateForMongoDB(defectCollection.getClosureDate());
		this.updatedBy = defectCollection.getUpdatedBy();
		this.raisedBy = defectCollection.getRaisedBy();
		this.owner = defectCollection.getOwner();
		this.assignedTo = defectCollection.getAssignedTo();
		this.lastUpdatedDate = setDateForMongoDB(defectCollection.getLastUpdatedDate());
		this.file = defectCollection.getFile();
		this.primaryFeature = defectCollection.getPrimaryFeature();
		this.primaryFeatureParent = defectCollection.getPrimaryFeatureParent();
		this.secondaryFeature = defectCollection.getSecondaryFeature();
		this.mapped = defectCollection.getMapped();
		this.testCases = defectCollection.getTestCases();
		this.confirmed = defectCollection.getConfirmed();
		this.testExecutionResultBugId = defectCollection.getTestExecutionResultBugId();
		this.testCaseExecutionResult = defectCollection.getTestCaseExecutionResult();
		this.environment = defectCollection.getEnvironment();
		this.closedDate = setDateForMongoDB(defectCollection.getClosedDate());
		this.resolvedDate = setDateForMongoDB(defectCollection.getResolvedDate());
		this.verifiedDate = setDateForMongoDB(defectCollection.getVerifiedDate());
		this.parentState = defectCollection.getParentState();
		this.driverState = defectCollection.getDriverState();
		this.specificationState = defectCollection.getSpecificationState();
		this.closedStatus = defectCollection.getClosedStatus();
		this.resolvedInfo = defectCollection.getResolvedInfo();
		this.remarks = defectCollection.getRemarks();
		this.type = defectCollection.getType();
		this.activityName = defectCollection.getActivityName();
		this.activityComponent = defectCollection.getActivityComponent();
	}
	
	public Integer getId() {
		return id;
	}

	public void set_id(Object _id) {
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

	public Object getRaisedDate() {
		return raisedDate;
	}

	public void setRaisedDate(Object raisedDate) {
		String formatCheck = raisedDate.toString();
		if(formatCheck.contains("date=")){
			try {
				raisedDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.raisedDate = raisedDate;
	}

	public Object getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(Object assignedDate) {
		String formatCheck = assignedDate.toString();
		if(formatCheck.contains("date=")){
			try {
				assignedDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
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

	public Object getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Object updatedDate) {
		String formatCheck = updatedDate.toString();
		if(formatCheck.contains("date=")){
			try {
				updatedDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.updatedDate = updatedDate;
	}

	public Float getExpectedEffort() {
		return expectedEffort;
	}

	public void setExpectedEffort(Float expectedEffort) {
		this.expectedEffort = expectedEffort;
	}

	public Object getPlannedClosureDate() {
		return plannedClosureDate;
	}

	public void setPlannedClosureDate(Object plannedClosureDate) {
		String formatCheck = plannedClosureDate.toString();
		if(formatCheck.contains("date=")){
			try {
				plannedClosureDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.plannedClosureDate = plannedClosureDate;
	}

	public Object getClosureDate() {
		return closureDate;
	}

	public void setClosureDate(Object closureDate) {
		String formatCheck = closureDate.toString();
		if(formatCheck.contains("date=")){
			try {
				closureDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
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

	public Object getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Object lastUpdatedDate) {
		String formatCheck = lastUpdatedDate.toString();
		if(formatCheck.contains("date=")){
			try {
				lastUpdatedDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
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

	public Object getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Object closedDate) {
		String formatCheck = closedDate.toString();
		if(formatCheck.contains("date=")){
			try {
				closedDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.closedDate = closedDate;
	}

	public Object getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(Object resolvedDate) {
		String formatCheck = resolvedDate.toString();
		if(formatCheck.contains("date=")){
			try {
				resolvedDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.resolvedDate = resolvedDate;
	}

	public Object getVerifiedDate() {
		return verifiedDate;
	}

	public void setVerifiedDate(Object verifiedDate) {
		String formatCheck = verifiedDate.toString();
		if(formatCheck.contains("date=")){
			try {
				verifiedDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
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
