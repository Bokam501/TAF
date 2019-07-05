package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hcl.atf.taf.mongodb.model.ReviewRecordCollectionMongo;

@Entity
@Table(name = "review_record_collection")
public class ReviewRecordCollection implements Serializable {

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
	
	@Column(name = "reviewRecordType")
	private String reviewRecordType;

	@Column(name = "workProductName")
	private String workProductName;

	@Column(name = "revisionNo")
	private String revisionNo;

	@Column(name = "reviewStartDate")
	private Date reviewStartDate;

	@Column(name = "reviewEndDate")
	private Date reviewEndDate;

	@Column(name = "major")
	private Integer major;

	@Column(name = "minor")
	private Integer minor;

	@Column(name = "trivial")
	private Integer trivial;

	@Column(name = "totalDefects")
	private Integer totalDefects;

	@Column(name = "totalAuthorEffort")
	private Float totalAuthorEffort;

	@Column(name = "totalReviewEffort")
	private Float totalReviewEffort;

	@Column(name = "totalReworkEffort")
	private Float totalReworkEffort;

	@Column(name = "workProductEffort")
	private String workProductEffort;

	@Column(name = "reviewNumber")
	private Integer reviewNumber;

	@Column(name = "commentNumber")
	private Integer commentNumber;

	@Column(name = "typeOfDefect")
	private String typeOfDefect;

	@Column(name = "severity")
	private String severity;

	@Column(name = "reviewerName")
	private String reviewerName;

	@Column(name = "issueLocation")
	private String issueLocation;

	@Column(name = "defectDescription")
	private String defectDescription;

	@Column(name = "actionTaken")
	private String actionTaken;

	@Column(name = "status")
	private String status;

	@Column(name = "causeOfDefect")
	private String causeOfDefect;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "correctiveAction")
	private String correctiveAction;

	@Column(name = "preventiveAction")
	private String preventiveAction;

	@Column(name = "owner")
	private String owner;
	
	@Column(name = "createdDate")
	private Date createdDate;
	
	@Column(name = "updateDate")
	private Date updateDate;

	@Column(name = "activityName")
	private String activityName;
	
	@Column(name = "activityComponent")
	private String activityComponent;
	
	public ReviewRecordCollection() {

		this._id = 0;
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

	public ReviewRecordCollection(ReviewRecordCollectionMongo reviewRecordCollectionMongo) {

		this._id = reviewRecordCollectionMongo.getId();
		this._class = this.getClass().getName().replace("\\", ".").replace("/", ".");
		this.testCentersName = reviewRecordCollectionMongo.getTestCentersName();
		this.testCentersId = reviewRecordCollectionMongo.getTestCentersId();
		this.testFactoryName = reviewRecordCollectionMongo.getTestFactoryName();
		this.testFactoryId = reviewRecordCollectionMongo.getTestFactoryId();
		this.customerName = reviewRecordCollectionMongo.getCustomerName();
		this.customerId = reviewRecordCollectionMongo.getCustomerId();
		this.productName = reviewRecordCollectionMongo.getProductName();
		this.productId = reviewRecordCollectionMongo.getProductId();
		this.productManager = reviewRecordCollectionMongo.getProductManager();
		this.project = reviewRecordCollectionMongo.getProject();
		this.projectId = reviewRecordCollectionMongo.getProjectId();
		this.projectManager = reviewRecordCollectionMongo.getProjectManager();
		this.competency = reviewRecordCollectionMongo.getCompetency();
		this.competencyId = reviewRecordCollectionMongo.getCompetencyId();
		this.competencyManager = reviewRecordCollectionMongo.getCompetencyManager();
		this.versionName = reviewRecordCollectionMongo.getVersionName();
		this.versionId = reviewRecordCollectionMongo.getVersionId();
		this.buildName = reviewRecordCollectionMongo.getBuildName();
		this.buildId = reviewRecordCollectionMongo.getBuildId();
		this.reviewRecordType = reviewRecordCollectionMongo.getReviewRecordType();
		this.workProductName = reviewRecordCollectionMongo.getWorkProductName();
		this.revisionNo = reviewRecordCollectionMongo.getRevisionNo();
		if (reviewRecordCollectionMongo.getReviewStartDate() != null) {
			this.reviewStartDate = (Date) reviewRecordCollectionMongo.getReviewStartDate();
		}
		if (reviewRecordCollectionMongo.getReviewEndDate() != null) {
			this.reviewEndDate = (Date) reviewRecordCollectionMongo.getReviewEndDate();

		}
		this.major = reviewRecordCollectionMongo.getMajor();
		this.minor = reviewRecordCollectionMongo.getMinor();
		this.trivial = reviewRecordCollectionMongo.getTrivial();
		this.totalDefects = reviewRecordCollectionMongo.getTotalDefects();
		this.totalAuthorEffort = reviewRecordCollectionMongo.getTotalAuthorEffort();
		this.totalReviewEffort = reviewRecordCollectionMongo.getTotalReviewEffort();
		this.totalReworkEffort = reviewRecordCollectionMongo.getTotalReworkEffort();
		this.workProductEffort = reviewRecordCollectionMongo.getWorkProductEffort();
		this.reviewNumber = reviewRecordCollectionMongo.getReviewNumber();
		this.commentNumber = reviewRecordCollectionMongo.getCommentNumber();
		this.typeOfDefect = reviewRecordCollectionMongo.getTypeOfDefect();
		this.severity = reviewRecordCollectionMongo.getSeverity();
		this.reviewerName = reviewRecordCollectionMongo.getReviewerName();
		this.issueLocation = reviewRecordCollectionMongo.getIssueLocation();
		this.defectDescription = reviewRecordCollectionMongo.getDefectDescription();
		this.actionTaken = reviewRecordCollectionMongo.getActionTaken();
		this.status = reviewRecordCollectionMongo.getStatus();
		this.causeOfDefect = reviewRecordCollectionMongo.getCauseOfDefect();
		this.remarks = reviewRecordCollectionMongo.getRemarks();
		this.correctiveAction = reviewRecordCollectionMongo.getCorrectiveAction();
		this.preventiveAction = reviewRecordCollectionMongo.getPreventiveAction();
		this.owner = reviewRecordCollectionMongo.getOwner();
		if (reviewRecordCollectionMongo.getReviewEndDate() != null) {
			this.createdDate = (Date) reviewRecordCollectionMongo.getCreatedDate();
		}
		if (reviewRecordCollectionMongo.getReviewEndDate() != null) {
			this.updateDate = (Date) reviewRecordCollectionMongo.getUpdateDate();
		}
		this.activityName = reviewRecordCollectionMongo.getActivityName();
		this.activityComponent = reviewRecordCollectionMongo.getActivityComponent();
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

	public String getReviewRecordType() {
		return reviewRecordType;
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

	public Date getReviewStartDate() {
		return reviewStartDate;
	}

	public void setReviewStartDate(Date reviewStartDate) {
		this.reviewStartDate = reviewStartDate;
	}

	public Date getReviewEndDate() {
		return reviewEndDate;
	}

	public void setReviewEndDate(Date reviewEndDate) {
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

}
