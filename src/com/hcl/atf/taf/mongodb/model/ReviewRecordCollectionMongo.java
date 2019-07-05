package com.hcl.atf.taf.mongodb.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Id;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ReviewRecordCollection;

@Document(collection = "review_record_collection")
public class ReviewRecordCollectionMongo {
	private static final Log log = LogFactory.getLog(ReviewRecordCollectionMongo.class);
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
	private String reviewRecordType;
	private String workProductName;
	private String revisionNo;
	private Object reviewStartDate;
	private Object reviewEndDate;
	private Integer major;
	private Integer minor;
	private Integer trivial;
	private Integer totalDefects;
	private Float totalAuthorEffort;
	private Float totalReviewEffort;
	private Float totalReworkEffort;
	private String workProductEffort;
	private Integer reviewNumber;
	private Integer commentNumber;
	private String typeOfDefect;
	private String severity;
	private String reviewerName;
	private String issueLocation;
	private String defectDescription;
	private String actionTaken;
	private String status;
	private String causeOfDefect;
	private String remarks;
	private String correctiveAction;
	private String preventiveAction;
	private String owner;
	private Object createdDate;
	private Object updateDate;
	private String activityName;
	private String activityComponent;
	
	public ReviewRecordCollectionMongo() {

		//this._id = 0;
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
		this.reviewRecordType = "";
		this.workProductName = "";
		this.revisionNo = "";
		this.reviewStartDate = null;
		this.reviewEndDate = null;
		this.major = 0;
		this.minor = 0;
		this.trivial = 0;
		this.totalDefects = 0;
		this.totalAuthorEffort = 0F;
		this.totalReviewEffort = 0F;
		this.totalReworkEffort = 0F;
		this.workProductEffort = "";
		this.reviewNumber = 0;
		this.commentNumber = 0;
		this.typeOfDefect = "";
		this.severity = "";
		this.reviewerName = "";
		this.issueLocation = "";
		this.defectDescription = "";
		this.actionTaken = "";
		this.status = "";
		this.causeOfDefect = "";
		this.remarks = "";
		this.correctiveAction = "";
		this.preventiveAction = "";
		this.owner = "";
		this.createdDate = null;
		this.updateDate = null;
		this.activityName = "";
		this.activityComponent = "";
	}

	public ReviewRecordCollectionMongo(ReviewRecordCollection reviewRecordCollection) {

		this.id = reviewRecordCollection.get_id();
		this._class = this.getClass().getName().replace("\\", ".").replace("/", ".");
		this.testCentersName = reviewRecordCollection.getTestCentersName();
		this.testCentersId = reviewRecordCollection.getTestCentersId();
		this.testFactoryName = reviewRecordCollection.getTestFactoryName();
		this.testFactoryId = reviewRecordCollection.getTestFactoryId();
		this.customerName = reviewRecordCollection.getCustomerName();
		this.customerId = reviewRecordCollection.getCustomerId();
		this.productName = reviewRecordCollection.getProductName();
		this.productId = reviewRecordCollection.getProductId();
		this.productManager = reviewRecordCollection.getProductManager();
		this.project = reviewRecordCollection.getProject();
		this.projectId = reviewRecordCollection.getProjectId();
		this.projectManager = reviewRecordCollection.getProjectManager();
		this.competency = reviewRecordCollection.getCompetency();
		this.competencyId = reviewRecordCollection.getCompetencyId();
		this.competencyManager = reviewRecordCollection.getCompetencyManager();
		this.versionName = reviewRecordCollection.getVersionName();
		this.versionId = reviewRecordCollection.getVersionId();
		this.buildName = reviewRecordCollection.getBuildName();
		this.buildId = reviewRecordCollection.getBuildId();
		this.reviewRecordType = reviewRecordCollection.getReviewRecordType();
		this.workProductName = reviewRecordCollection.getWorkProductName();
		this.revisionNo = reviewRecordCollection.getRevisionNo();
		this.reviewStartDate = setDateForMongoDB(reviewRecordCollection.getReviewStartDate());
		this.reviewEndDate = setDateForMongoDB(reviewRecordCollection.getReviewEndDate());
		this.major = reviewRecordCollection.getMajor();
		this.minor = reviewRecordCollection.getMinor();
		this.trivial = reviewRecordCollection.getTrivial();
		this.totalDefects = reviewRecordCollection.getTotalDefects();
		this.totalAuthorEffort = reviewRecordCollection.getTotalAuthorEffort();
		this.totalReviewEffort = reviewRecordCollection.getTotalReviewEffort();
		this.totalReworkEffort = reviewRecordCollection.getTotalReworkEffort();
		this.workProductEffort = reviewRecordCollection.getWorkProductEffort();
		this.reviewNumber = reviewRecordCollection.getReviewNumber();
		this.commentNumber = reviewRecordCollection.getCommentNumber();
		this.typeOfDefect = reviewRecordCollection.getTypeOfDefect();
		this.severity = reviewRecordCollection.getSeverity();
		this.reviewerName = reviewRecordCollection.getReviewerName();
		this.issueLocation = reviewRecordCollection.getIssueLocation();
		this.defectDescription = reviewRecordCollection.getDefectDescription();
		this.actionTaken = reviewRecordCollection.getActionTaken();
		this.status = reviewRecordCollection.getStatus();
		this.causeOfDefect = reviewRecordCollection.getCauseOfDefect();
		this.remarks = reviewRecordCollection.getRemarks();
		this.correctiveAction = reviewRecordCollection.getCorrectiveAction();
		this.preventiveAction = reviewRecordCollection.getPreventiveAction();
		this.owner = reviewRecordCollection.getOwner();
		this.createdDate = setDateForMongoDB(reviewRecordCollection.getCreatedDate());
		this.updateDate = setDateForMongoDB(reviewRecordCollection.getUpdateDate());
		this.activityName = reviewRecordCollection.getActivityName();
		this.activityComponent = reviewRecordCollection.getActivityComponent();
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

	public String getReviewRecordType() {
		return reviewRecordType;
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

	public void setReviewRecordType(String reviewRecordType) {
		this.reviewRecordType = reviewRecordType;
	}

	public String getWorkProductName() {
		return workProductName;
	}

	public void setWorkProductName(String workProductName) {
		this.workProductName = workProductName;
	}

	public String getRevisionNo() {
		return revisionNo;
	}

	public void setRevisionNo(String revisionNo) {
		this.revisionNo = revisionNo;
	}

	public Object getReviewStartDate() {
		return reviewStartDate;
	}

	public void setReviewStartDate(Object reviewStartDate) {
		String formatCheck = reviewStartDate.toString();
		if(formatCheck.contains("date=")){
			try {
				reviewStartDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.reviewStartDate = reviewStartDate;
	}

	public Object getReviewEndDate() {
		return reviewEndDate;
	}

	public void setReviewEndDate(Object reviewEndDate) {
		String formatCheck = reviewEndDate.toString();
		if(formatCheck.contains("date=")){
			try {
				reviewEndDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.reviewEndDate = reviewEndDate;
	}

	public Integer getMajor() {
		return major;
	}

	public void setMajor(Integer major) {
		this.major = major;
	}

	public Integer getMinor() {
		return minor;
	}

	public void setMinor(Integer minor) {
		this.minor = minor;
	}

	public Integer getTrivial() {
		return trivial;
	}

	public void setTrivial(Integer trivial) {
		this.trivial = trivial;
	}

	public Integer getTotalDefects() {
		return totalDefects;
	}

	public void setTotalDefects(Integer totalDefects) {
		this.totalDefects = totalDefects;
	}

	public Float getTotalAuthorEffort() {
		return totalAuthorEffort;
	}

	public void setTotalAuthorEffort(Float totalAuthorEffort) {
		this.totalAuthorEffort = totalAuthorEffort;
	}

	public Float getTotalReviewEffort() {
		return totalReviewEffort;
	}

	public void setTotalReviewEffort(Float totalReviewEffort) {
		this.totalReviewEffort = totalReviewEffort;
	}

	public Float getTotalReworkEffort() {
		return totalReworkEffort;
	}

	public void setTotalReworkEffort(Float totalReworkEffort) {
		this.totalReworkEffort = totalReworkEffort;
	}

	public String getWorkProductEffort() {
		return workProductEffort;
	}

	public void setWorkProductEffort(String workProductEffort) {
		this.workProductEffort = workProductEffort;
	}

	public Integer getReviewNumber() {
		return reviewNumber;
	}

	public void setReviewNumber(Integer reviewNumber) {
		this.reviewNumber = reviewNumber;
	}

	public Integer getCommentNumber() {
		return commentNumber;
	}

	public void setCommentNumber(Integer commentNumber) {
		this.commentNumber = commentNumber;
	}

	public String getTypeOfDefect() {
		return typeOfDefect;
	}

	public void setTypeOfDefect(String typeOfDefect) {
		this.typeOfDefect = typeOfDefect;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getReviewerName() {
		return reviewerName;
	}

	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}

	public String getIssueLocation() {
		return issueLocation;
	}

	public void setIssueLocation(String issueLocation) {
		this.issueLocation = issueLocation;
	}

	public String getDefectDescription() {
		return defectDescription;
	}

	public void setDefectDescription(String defectDescription) {
		this.defectDescription = defectDescription;
	}

	public String getActionTaken() {
		return actionTaken;
	}

	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCauseOfDefect() {
		return causeOfDefect;
	}

	public void setCauseOfDefect(String causeOfDefect) {
		this.causeOfDefect = causeOfDefect;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCorrectiveAction() {
		return correctiveAction;
	}

	public void setCorrectiveAction(String correctiveAction) {
		this.correctiveAction = correctiveAction;
	}

	public String getPreventiveAction() {
		return preventiveAction;
	}

	public void setPreventiveAction(String preventiveAction) {
		this.preventiveAction = preventiveAction;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
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