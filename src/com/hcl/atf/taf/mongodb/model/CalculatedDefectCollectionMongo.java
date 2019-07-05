package com.hcl.atf.taf.mongodb.model;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.persistence.Id;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "calculated_defect_collection")
public class CalculatedDefectCollectionMongo {
	private static final Log log = LogFactory.getLog(CalculatedDefectCollectionMongo.class);

	@Transient
	private SimpleDateFormat dateFormatForMongoDB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

	@Id
	private String id;
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
	private Float cumulativeTotal;
	private Float cumulativeClosed;
	private Float cumulativeInvalid;
	private Float cumulativeNonQulityDefects;
	private Float cumulativeLeakedDefects;
	private String category;
	private Float l0ResourceCount;
	private Float l1ResourceCount;
	private Float l2ResourceCount;
	private Float l3ResourceCount;
	private Float l4ResourceCount;
	private Float l5ResourceCount;
	private Float l6ResourceCount;
	private Float l7ResourceCount;
	private String remarks;

	public CalculatedDefectCollectionMongo() {

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
		this.cumulativeTotal = 0.0F;
		this.cumulativeClosed = 0.0F;
		this.cumulativeInvalid = 0.0F;
		this.cumulativeNonQulityDefects = 0.0F;
		this.cumulativeLeakedDefects = 0.0F;
		this.category = "";
		this.l0ResourceCount = 0.0F;
		this.l1ResourceCount = 0.0F;
		this.l2ResourceCount = 0.0F;
		this.l3ResourceCount = 0.0F;
		this.l4ResourceCount = 0.0F;
		this.l5ResourceCount = 0.0F;
		this.l6ResourceCount = 0.0F;
		this.l7ResourceCount = 0.0F;
		this.remarks = "";
	}

	public String getId() {
		return id;
	}

	public void setId(Object _id) {
		String formatCheck = _id.toString();
		if(formatCheck.contains("oid=")){
			_id = formatCheck.split("oid=")[1].split("}")[0];
		}
		this.id = _id.toString();
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
		if (formatCheck.contains("date=")) {
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
		if (formatCheck.contains("date=")) {
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
		if (formatCheck.contains("date=")) {
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

	public Float getCumulativeTotal() {
		return cumulativeTotal;
	}

	public void setCumulativeTotal(Float cumulativeTotal) {
		this.cumulativeTotal = cumulativeTotal;
	}

	public Float getCumulativeClosed() {
		return cumulativeClosed;
	}

	public void setCumulativeClosed(Float cumulativeClosed) {
		this.cumulativeClosed = cumulativeClosed;
	}

	public Float getCumulativeInvalid() {
		return cumulativeInvalid;
	}

	public void setCumulativeInvalid(Float cumulativeInvalid) {
		this.cumulativeInvalid = cumulativeInvalid;
	}

	public Float getCumulativeNonQulityDefects() {
		return cumulativeNonQulityDefects;
	}

	public void setCumulativeNonQulityDefects(Float cumulativeNonQulityDefects) {
		this.cumulativeNonQulityDefects = cumulativeNonQulityDefects;
	}

	public Float getCumulativeLeakedDefects() {
		return cumulativeLeakedDefects;
	}

	public void setCumulativeLeakedDefects(Float cumulativeLeakedDefects) {
		this.cumulativeLeakedDefects = cumulativeLeakedDefects;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

}
