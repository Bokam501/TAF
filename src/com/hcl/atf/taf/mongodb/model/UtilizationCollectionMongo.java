package com.hcl.atf.taf.mongodb.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.annotation.Transient;

import com.hcl.atf.taf.model.UtilizationCollection;

public class UtilizationCollectionMongo {
	private static final Log log = LogFactory.getLog(UtilizationCollectionMongo.class);
	@Transient
	private SimpleDateFormat dateFormatForMongoDB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
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
	private Object createdDate;
	private Object updatedDate;
	private Object startDate;
	private Object endDate;
	private String resourceName;
	private String moduleName;
	private String activityName;
	private String activityType;
	private Float activityEffort;
	private String bucketType;

	public UtilizationCollectionMongo() {

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
		this.activityEffort = 0F;
		this.bucketType = "";

	}

	public UtilizationCollectionMongo(UtilizationCollection utilizationCollection) {

		this.id = utilizationCollection.get_id();
		this._class = this.getClass().getName().replace("\\", ".").replace("/", ".");
		this.testCentersName = utilizationCollection.getTestCentersName();
		this.testCentersId = utilizationCollection.getTestCentersId();
		this.testFactoryName = utilizationCollection.getTestFactoryName();
		this.testFactoryId = utilizationCollection.getTestFactoryId();
		this.customerName = utilizationCollection.getCustomerName();
		this.customerId = utilizationCollection.getCustomerId();
		this.productName = utilizationCollection.getProductName();
		this.productId = utilizationCollection.getProductId();
		this.productManager = utilizationCollection.getProductManager();
		this.project = utilizationCollection.getProject();
		this.projectId = utilizationCollection.getProjectId();
		this.projectManager = utilizationCollection.getProjectManager();
		this.competency = utilizationCollection.getCompetency();
		this.competencyId = utilizationCollection.getCompetencyId();
		this.competencyManager = utilizationCollection.getCompetencyManager();
		this.versionName = utilizationCollection.getVersionName();
		this.versionId = utilizationCollection.getVersionId();
		this.buildName = utilizationCollection.getBuildName();
		this.buildId = utilizationCollection.getBuildId();
		this.createdDate = setDateForMongoDB(utilizationCollection.getCreatedDate());
		this.updatedDate = setDateForMongoDB(utilizationCollection.getUpdatedDate());
		this.startDate = setDateForMongoDB(utilizationCollection.getStartDate());
		this.endDate = setDateForMongoDB(utilizationCollection.getEndDate());
		this.resourceName = utilizationCollection.getResourceName();
		this.moduleName = utilizationCollection.getModuleName();
		this.activityName = utilizationCollection.getActivityName();
		this.activityType = utilizationCollection.getActivityType();
		this.activityEffort = utilizationCollection.getActivityEffort();
		this.bucketType = utilizationCollection.getBucketType();

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer _id) {
		this.id = _id;
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

	public Object getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Object createdDate) {

		String formatCheck = createdDate.toString();
		if (formatCheck.contains("date=")) {
			try {
				createdDate = dateFormatForMongoDB.parse(formatCheck
						.split("date=")[1].split("}")[0]);
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

		String formatCheck = createdDate.toString();
		if (formatCheck.contains("date=")) {
			try {
				createdDate = dateFormatForMongoDB.parse(formatCheck
						.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}

		this.updatedDate = updatedDate;
	}

	public Object getStartDate() {
		return startDate;
	}

	public void setStartDate(Object startDate) {

		String formatCheck = createdDate.toString();
		if (formatCheck.contains("date=")) {
			try {
				createdDate = dateFormatForMongoDB.parse(formatCheck
						.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}

		this.startDate = startDate;
	}

	public Object getEndDate() {
		return endDate;
	}

	public void setEndDate(Object endDate) {
		this.endDate = endDate;

		String formatCheck = createdDate.toString();
		if (formatCheck.contains("date=")) {
			try {
				createdDate = dateFormatForMongoDB.parse(formatCheck
						.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}

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
