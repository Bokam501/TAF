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

import com.hcl.atf.taf.model.DPAWorkbookCollection;

@Document(collection = "activity_collection")
public class DPAWorkbookCollectionMongo {
	private static final Log log = LogFactory.getLog(DPAWorkbookCollectionMongo.class);
	@Transient
	private SimpleDateFormat dateFormatForMongoDB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

	@Id
	private String id;
	private String _class;
	private Integer referenceId;
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
	private String dpaId;
	private Object meetingReferenceDate;
	private String defectType;
	private Integer defectCount;
	private String analysis;
	private String primaryCause;
	private String correctiveDescription;
	private Object correctivePlannedStartDate;
	private Object correctivePlannedEndDate;
	private Object correctiveActualStartDate;
	private Object correctiveActualEndDate;
	private String correctiveClosureResponsibility;
	private String correctiveActionStatus;
	private String correctiveEvaluatingEffectiveness;
	private Object correctiveEvaluatingDate;
	private String correctiveEffectiveness;
	private String preventiveDescription;
	private Object preventivePlannedStartDate;
	private Object preventivePlannedEndDate;
	private Object preventiveActualStartDate;
	private Object preventiveActualEndDate;
	private String preventiveClosureResponsibility;
	private String preventiveActionStatus;
	private String preventiveEvaluatingEffectiveness;
	private Object preventiveEvaluatingDate;
	private String preventiveEffectiveness;
	private String remarks;
	private Object createdDate;
	private Object updateDate;

	public DPAWorkbookCollectionMongo(){
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
		this.dpaId = "";
		this.meetingReferenceDate = null;
		this.defectType = "";
		this.defectCount = 0;
		this.analysis = "";
		this.primaryCause = "";
		this.correctiveDescription = "";
		this.correctivePlannedStartDate = null;
		this.correctivePlannedEndDate = null;
		this.correctiveActualStartDate = null;
		this.correctiveActualEndDate = null;
		this.correctiveClosureResponsibility = "";
		this.correctiveActionStatus = "";
		this.correctiveEvaluatingEffectiveness = "";
		this.correctiveEvaluatingDate = null;
		this.correctiveEffectiveness = "";
		this.preventiveDescription = "";
		this.preventivePlannedStartDate = null;
		this.preventivePlannedEndDate = null;
		this.preventiveActualStartDate = null;
		this.preventiveActualEndDate = null;
		this.preventiveClosureResponsibility = "";
		this.preventiveActionStatus = "";
		this.preventiveEvaluatingEffectiveness = "";
		this.preventiveEvaluatingDate = null;
		this.preventiveEffectiveness = "";
		this.remarks = "";
		this.createdDate = null;
		this.updateDate = null;
	}

	public DPAWorkbookCollectionMongo(DPAWorkbookCollection dpaWorkbookCollection){
		this.id = dpaWorkbookCollection.getDpaId();
		this._class = this.getClass().getName().replace("\\", ".").replace("/", ".");
		this.referenceId = dpaWorkbookCollection.get_id();
		this.testCentersName = dpaWorkbookCollection.getTestCentersName();
		this.testCentersId = dpaWorkbookCollection.getTestCentersId();
		this.testFactoryName = dpaWorkbookCollection.getTestFactoryName();
		this.testFactoryId = dpaWorkbookCollection.getTestFactoryId();
		this.customerName = dpaWorkbookCollection.getCustomerName();
		this.customerId = dpaWorkbookCollection.getCustomerId();
		this.productName = dpaWorkbookCollection.getProductName();
		this.productId = dpaWorkbookCollection.getProductId();
		this.productManager = dpaWorkbookCollection.getProductManager();
		this.project = dpaWorkbookCollection.getProject();
		this.projectId = dpaWorkbookCollection.getProjectId();
		this.projectManager = dpaWorkbookCollection.getProjectManager();
		this.competency = dpaWorkbookCollection.getCompetency();
		this.competencyId = dpaWorkbookCollection.getCompetencyId();
		this.competencyManager = dpaWorkbookCollection.getCompetencyManager();
		this.versionName = dpaWorkbookCollection.getVersionName();
		this.versionId = dpaWorkbookCollection.getVersionId();
		this.buildName = dpaWorkbookCollection.getBuildName();
		this.buildId = dpaWorkbookCollection.getBuildId();
		this.dpaId = dpaWorkbookCollection.getDpaId();
		this.meetingReferenceDate = setDateForMongoDB(dpaWorkbookCollection.getMeetingReferenceDate());
		this.defectType = dpaWorkbookCollection.getDefectType();
		this.defectCount = dpaWorkbookCollection.getDefectCount();
		this.analysis = dpaWorkbookCollection.getAnalysis();
		this.primaryCause = dpaWorkbookCollection.getPrimaryCause();
		this.correctiveDescription = dpaWorkbookCollection.getCorrectiveDescription();
		this.correctivePlannedStartDate = setDateForMongoDB(dpaWorkbookCollection.getCorrectivePlannedStartDate());
		this.correctivePlannedEndDate = setDateForMongoDB(dpaWorkbookCollection.getCorrectivePlannedEndDate());
		this.correctiveActualStartDate = setDateForMongoDB(dpaWorkbookCollection.getCorrectiveActualStartDate());
		this.correctiveActualEndDate = setDateForMongoDB(dpaWorkbookCollection.getCorrectiveActualEndDate());
		this.correctiveClosureResponsibility = dpaWorkbookCollection.getCorrectiveClosureResponsibility();
		this.correctiveActionStatus = dpaWorkbookCollection.getCorrectiveActionStatus();
		this.correctiveEvaluatingEffectiveness = dpaWorkbookCollection.getCorrectiveEvaluatingEffectiveness();
		this.correctiveEvaluatingDate = setDateForMongoDB(dpaWorkbookCollection.getCorrectiveEvaluatingDate());
		this.correctiveEffectiveness = dpaWorkbookCollection.getCorrectiveEffectiveness();
		this.preventiveDescription = dpaWorkbookCollection.getPreventiveDescription();
		this.preventivePlannedStartDate = setDateForMongoDB(dpaWorkbookCollection.getPreventivePlannedStartDate());
		this.preventivePlannedEndDate = setDateForMongoDB(dpaWorkbookCollection.getPreventivePlannedEndDate());
		this.preventiveActualStartDate = setDateForMongoDB(dpaWorkbookCollection.getPreventiveActualStartDate());
		this.preventiveActualEndDate = setDateForMongoDB(dpaWorkbookCollection.getPreventiveActualEndDate());
		this.preventiveClosureResponsibility = dpaWorkbookCollection.getPreventiveClosureResponsibility();
		this.preventiveActionStatus = dpaWorkbookCollection.getPreventiveActionStatus();
		this.preventiveEvaluatingEffectiveness = dpaWorkbookCollection.getPreventiveEvaluatingEffectiveness();
		this.preventiveEvaluatingDate = setDateForMongoDB(dpaWorkbookCollection.getPreventiveEvaluatingDate());
		this.preventiveEffectiveness = dpaWorkbookCollection.getPreventiveEffectiveness();
		this.remarks = dpaWorkbookCollection.getRemarks();
		this.createdDate = setDateForMongoDB(dpaWorkbookCollection.getCreatedDate());
		this.updateDate = setDateForMongoDB(dpaWorkbookCollection.getUpdateDate());
		
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

	public Integer getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
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

	public String getDpaId() {
		return dpaId;
	}

	public void setDpaId(String dpaId) {
		this.dpaId = dpaId;
	}

	public Object getMeetingReferenceDate() {
		return meetingReferenceDate;
	}

	public void setMeetingReferenceDate(Object meetingReferenceDate) {
		String formatCheck = meetingReferenceDate.toString();
		if(formatCheck.contains("date=")){
			try {
				meetingReferenceDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.meetingReferenceDate = meetingReferenceDate;
	}

	public String getDefectType() {
		return defectType;
	}

	public void setDefectType(String defectType) {
		this.defectType = defectType;
	}

	public Integer getDefectCount() {
		return defectCount;
	}

	public void setDefectCount(Integer defectCount) {
		this.defectCount = defectCount;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public String getPrimaryCause() {
		return primaryCause;
	}

	public void setPrimaryCause(String primaryCause) {
		this.primaryCause = primaryCause;
	}

	public String getCorrectiveDescription() {
		return correctiveDescription;
	}

	public void setCorrectiveDescription(String correctiveDescription) {
		this.correctiveDescription = correctiveDescription;
	}

	public Object getCorrectivePlannedStartDate() {
		return correctivePlannedStartDate;
	}

	public void setCorrectivePlannedStartDate(Object correctivePlannedStartDate) {
		String formatCheck = correctivePlannedStartDate.toString();
		if(formatCheck.contains("date=")){
			try {
				correctivePlannedStartDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.correctivePlannedStartDate = correctivePlannedStartDate;
	}

	public Object getCorrectivePlannedEndDate() {
		return correctivePlannedEndDate;
	}

	public void setCorrectivePlannedEndDate(Object correctivePlannedEndDate) {
		String formatCheck = correctivePlannedEndDate.toString();
		if(formatCheck.contains("date=")){
			try {
				correctivePlannedEndDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.correctivePlannedEndDate = correctivePlannedEndDate;
	}

	public Object getCorrectiveActualStartDate() {
		return correctiveActualStartDate;
	}

	public void setCorrectiveActualStartDate(Object correctiveActualStartDate) {
		String formatCheck = correctiveActualStartDate.toString();
		if(formatCheck.contains("date=")){
			try {
				correctiveActualStartDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.correctiveActualStartDate = correctiveActualStartDate;
	}

	public Object getCorrectiveActualEndDate() {
		return correctiveActualEndDate;
	}

	public void setCorrectiveActualEndDate(Object correctiveActualEndDate) {
		String formatCheck = correctiveActualEndDate.toString();
		if(formatCheck.contains("date=")){
			try {
				correctiveActualEndDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.correctiveActualEndDate = correctiveActualEndDate;
	}

	public String getCorrectiveClosureResponsibility() {
		return correctiveClosureResponsibility;
	}

	public void setCorrectiveClosureResponsibility(
			String correctiveClosureResponsibility) {
		this.correctiveClosureResponsibility = correctiveClosureResponsibility;
	}

	public String getCorrectiveActionStatus() {
		return correctiveActionStatus;
	}

	public void setCorrectiveActionStatus(String correctiveActionStatus) {
		this.correctiveActionStatus = correctiveActionStatus;
	}

	public String getCorrectiveEvaluatingEffectiveness() {
		return correctiveEvaluatingEffectiveness;
	}

	public void setCorrectiveEvaluatingEffectiveness(
			String correctiveEvaluatingEffectiveness) {
		this.correctiveEvaluatingEffectiveness = correctiveEvaluatingEffectiveness;
	}

	public Object getCorrectiveEvaluatingDate() {
		return correctiveEvaluatingDate;
	}

	public void setCorrectiveEvaluatingDate(Object correctiveEvaluatingDate) {
		String formatCheck = correctiveEvaluatingDate.toString();
		if(formatCheck.contains("date=")){
			try {
				correctiveEvaluatingDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.correctiveEvaluatingDate = correctiveEvaluatingDate;
	}

	public String getCorrectiveEffectiveness() {
		return correctiveEffectiveness;
	}

	public void setCorrectiveEffectiveness(String correctiveEffectiveness) {
		this.correctiveEffectiveness = correctiveEffectiveness;
	}

	public String getPreventiveDescription() {
		return preventiveDescription;
	}

	public void setPreventiveDescription(String preventiveDescription) {
		this.preventiveDescription = preventiveDescription;
	}

	public Object getPreventivePlannedStartDate() {
		return preventivePlannedStartDate;
	}

	public void setPreventivePlannedStartDate(Object preventivePlannedStartDate) {
		String formatCheck = preventivePlannedStartDate.toString();
		if(formatCheck.contains("date=")){
			try {
				preventivePlannedStartDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.preventivePlannedStartDate = preventivePlannedStartDate;
	}

	public Object getPreventivePlannedEndDate() {
		return preventivePlannedEndDate;
	}

	public void setPreventivePlannedEndDate(Object preventivePlannedEndDate) {
		String formatCheck = preventivePlannedEndDate.toString();
		if(formatCheck.contains("date=")){
			try {
				preventivePlannedEndDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.preventivePlannedEndDate = preventivePlannedEndDate;
	}

	public Object getPreventiveActualStartDate() {
		return preventiveActualStartDate;
	}

	public void setPreventiveActualStartDate(Object preventiveActualStartDate) {
		String formatCheck = preventiveActualStartDate.toString();
		if(formatCheck.contains("date=")){
			try {
				preventiveActualStartDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.preventiveActualStartDate = preventiveActualStartDate;
	}

	public Object getPreventiveActualEndDate() {
		return preventiveActualEndDate;
	}

	public void setPreventiveActualEndDate(Object preventiveActualEndDate) {
		String formatCheck = preventiveActualEndDate.toString();
		if(formatCheck.contains("date=")){
			try {
				preventiveActualEndDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.preventiveActualEndDate = preventiveActualEndDate;
	}

	public String getPreventiveClosureResponsibility() {
		return preventiveClosureResponsibility;
	}

	public void setPreventiveClosureResponsibility(
			String preventiveClosureResponsibility) {
		this.preventiveClosureResponsibility = preventiveClosureResponsibility;
	}

	public String getPreventiveActionStatus() {
		return preventiveActionStatus;
	}

	public void setPreventiveActionStatus(String preventiveActionStatus) {
		this.preventiveActionStatus = preventiveActionStatus;
	}

	public String getPreventiveEvaluatingEffectiveness() {
		return preventiveEvaluatingEffectiveness;
	}

	public void setPreventiveEvaluatingEffectiveness(
			String preventiveEvaluatingEffectiveness) {
		this.preventiveEvaluatingEffectiveness = preventiveEvaluatingEffectiveness;
	}

	public Object getPreventiveEvaluatingDate() {
		return preventiveEvaluatingDate;
	}

	public void setPreventiveEvaluatingDate(Object preventiveEvaluatingDate) {
		String formatCheck = preventiveEvaluatingDate.toString();
		if(formatCheck.contains("date=")){
			try {
				preventiveEvaluatingDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.preventiveEvaluatingDate = preventiveEvaluatingDate;
	}

	public String getPreventiveEffectiveness() {
		return preventiveEffectiveness;
	}

	public void setPreventiveEffectiveness(String preventiveEffectiveness) {
		this.preventiveEffectiveness = preventiveEffectiveness;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
