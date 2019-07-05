package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hcl.atf.taf.mongodb.model.UtilizationCollectionMongo;

@Entity
@Table(name = "utilization_collection")
public class UtilizationCollection implements Serializable {

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

	@Column(name = "startDate")
	private Date startDate;

	@Column(name = "endDate")
	private Date endDate;
	
	@Column(name = "createdDate")
	private Date createdDate;

	@Column(name = "updatedDate")
	private Date updatedDate;

	@Column(name = "resourceName")
	private String resourceName;

	@Column(name = "moduleName")
	private String moduleName;

	@Column(name = "activityName")
	private String activityName;

	@Column(name = "activityType")
	private String activityType;

	@Column(name = "activityEffort")
	private Float activityEffort;

	@Column(name = "bucketType")
	private String bucketType;
	
	public UtilizationCollection() {
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
		this.createdDate = null;
		this.updatedDate = null;
		this.startDate = null;
		this.endDate = null;
		this.resourceName = "";
		this.moduleName = "";
		this.activityName = "";
		this.activityType = "";
		this.activityEffort = 0.0F;
		this.bucketType = "";
	}

	public UtilizationCollection(UtilizationCollectionMongo utilizationCollectionMongo) {

		this._id = utilizationCollectionMongo.getId();
		this._class = this.getClass().getName().replace("\\", ".").replace("/", ".");
		this.testCentersName = utilizationCollectionMongo.getTestCentersName();
		this.testCentersId = utilizationCollectionMongo.getTestCentersId();
		this.testFactoryName = utilizationCollectionMongo.getTestFactoryName();
		this.testFactoryId = utilizationCollectionMongo.getTestFactoryId();
		this.customerName = utilizationCollectionMongo.getCustomerName();
		this.customerId = utilizationCollectionMongo.getCustomerId();
		this.productName = utilizationCollectionMongo.getProductName();
		this.productId = utilizationCollectionMongo.getProductId();
		this.productManager = utilizationCollectionMongo.getProductManager();
		this.project = utilizationCollectionMongo.getProject();
		this.projectId = utilizationCollectionMongo.getProjectId();
		this.projectManager = utilizationCollectionMongo.getProjectManager();
		this.competency = utilizationCollectionMongo.getCompetency();
		this.competencyId = utilizationCollectionMongo.getCompetencyId();
		this.competencyManager = utilizationCollectionMongo.getCompetencyManager();
		this.versionName = utilizationCollectionMongo.getVersionName();
		this.versionId = utilizationCollectionMongo.getVersionId();
		this.buildName = utilizationCollectionMongo.getBuildName();
		this.buildId = utilizationCollectionMongo.getBuildId();

		if (utilizationCollectionMongo.getCreatedDate() != null) {
			this.createdDate = (Date) utilizationCollectionMongo.getCreatedDate();
		}

		if (utilizationCollectionMongo.getUpdatedDate() != null) {
			this.updatedDate = (Date) utilizationCollectionMongo.getUpdatedDate();
		}

		if (utilizationCollectionMongo.getStartDate() != null) {
			this.startDate = (Date) utilizationCollectionMongo.getStartDate();
		}

		if (utilizationCollectionMongo.getEndDate() != null) {
			this.endDate = (Date) utilizationCollectionMongo.getEndDate();
		}
		this.resourceName = utilizationCollectionMongo.getResourceName();
		this.moduleName = utilizationCollectionMongo.getModuleName();
		this.activityName = utilizationCollectionMongo.getActivityName();
		this.activityType = utilizationCollectionMongo.getActivityType();
		this.activityEffort = utilizationCollectionMongo.getActivityEffort();
		this.bucketType = utilizationCollectionMongo.getBucketType();

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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public Float getActivityEffort() {
		return activityEffort;
	}

	public void setActivityEffort(Float activityEffort) {
		this.activityEffort = activityEffort;
	}

	public String getBucketType() {
		return bucketType;
	}

	public void setBucketType(String bucketType) {
		this.bucketType = bucketType;
	}

}
