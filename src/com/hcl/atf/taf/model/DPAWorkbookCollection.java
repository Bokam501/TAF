package com.hcl.atf.taf.model;


import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hcl.atf.taf.mongodb.model.DPAWorkbookCollectionMongo;

@Entity
@Table(name = "dpa_workbook_collection")
public class DPAWorkbookCollection implements Serializable {

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
	
	@Column(name = "dpaId")
	private String dpaId;
	
	@Column(name = "meetingReferenceDate")
	private Date meetingReferenceDate;
	
	@Column(name = "defectType")
	private String defectType;
	
	@Column(name = "defectCount")
	private Integer defectCount;
	
	@Column(name = "analysis")
	private String analysis;
	
	@Column(name = "primaryCause")
	private String primaryCause;
	
	@Column(name = "correctiveDescription")
	private String correctiveDescription;
	
	@Column(name = "correctivePlannedStartDate")
	private Date correctivePlannedStartDate;
	
	@Column(name = "correctivePlannedEndDate")
	private Date correctivePlannedEndDate;
	
	@Column(name = "correctiveActualStartDate")
	private Date correctiveActualStartDate;
	
	@Column(name = "correctiveActualEndDate")
	private Date correctiveActualEndDate;
	
	@Column(name = "correctiveClosureResponsibility")
	private String correctiveClosureResponsibility;
	
	@Column(name = "correctiveActionStatus")
	private String correctiveActionStatus;
	
	@Column(name = "correctiveEvaluatingEffectiveness")
	private String correctiveEvaluatingEffectiveness;
	
	@Column(name = "correctiveEvaluatingDate")
	private Date correctiveEvaluatingDate;
	
	@Column(name = "correctiveEffectiveness")
	private String correctiveEffectiveness;
		
	@Column(name = "preventiveDescription")
	private String preventiveDescription;
	
	@Column(name = "preventivePlannedStartDate")
	private Date preventivePlannedStartDate;
	
	@Column(name = "preventivePlannedEndDate")
	private Date preventivePlannedEndDate;
	
	@Column(name = "preventiveActualStartDate")
	private Date preventiveActualStartDate;
	
	@Column(name = "preventiveActualEndDate")
	private Date preventiveActualEndDate;
	
	@Column(name = "preventiveClosureResponsibility")
	private String preventiveClosureResponsibility;
	
	@Column(name = "preventiveActionStatus")
	private String preventiveActionStatus;
	
	@Column(name = "preventiveEvaluatingEffectiveness")
	private String preventiveEvaluatingEffectiveness;
	
	@Column(name = "preventiveEvaluatingDate")
	private Date preventiveEvaluatingDate;
	
	@Column(name = "preventiveEffectiveness")
	private String preventiveEffectiveness;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "createdDate")
	private Date createdDate;
	
	@Column(name = "updateDate")
	private Date updateDate;

	public DPAWorkbookCollection(){
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

	public DPAWorkbookCollection(DPAWorkbookCollectionMongo dpaWorkbookCollectionMongo){
		this._id = dpaWorkbookCollectionMongo.getReferenceId();
		this._class = this.getClass().getName().replace("\\", ".").replace("/", ".");
		this.testCentersName = dpaWorkbookCollectionMongo.getTestCentersName();
		this.testCentersId = dpaWorkbookCollectionMongo.getTestCentersId();
		this.testFactoryName = dpaWorkbookCollectionMongo.getTestFactoryName();
		this.testFactoryId = dpaWorkbookCollectionMongo.getTestFactoryId();
		this.customerName = dpaWorkbookCollectionMongo.getCustomerName();
		this.customerId = dpaWorkbookCollectionMongo.getCustomerId();
		this.productName = dpaWorkbookCollectionMongo.getProductName();
		this.productId = dpaWorkbookCollectionMongo.getProductId();
		this.productManager = dpaWorkbookCollectionMongo.getProductManager();
		this.project = dpaWorkbookCollectionMongo.getProject();
		this.projectId = dpaWorkbookCollectionMongo.getProjectId();
		this.projectManager = dpaWorkbookCollectionMongo.getProjectManager();
		this.competency = dpaWorkbookCollectionMongo.getCompetency();
		this.competencyId = dpaWorkbookCollectionMongo.getCompetencyId();
		this.competencyManager = dpaWorkbookCollectionMongo.getCompetencyManager();
		this.versionName = dpaWorkbookCollectionMongo.getVersionName();
		this.versionId = dpaWorkbookCollectionMongo.getVersionId();
		this.buildName = dpaWorkbookCollectionMongo.getBuildName();
		this.buildId = dpaWorkbookCollectionMongo.getBuildId();
		this.dpaId = dpaWorkbookCollectionMongo.getDpaId();
		if(dpaWorkbookCollectionMongo.getMeetingReferenceDate() != null){
			this.meetingReferenceDate = (Date) dpaWorkbookCollectionMongo.getMeetingReferenceDate();
		}
		this.defectType = dpaWorkbookCollectionMongo.getDefectType();
		this.defectCount = dpaWorkbookCollectionMongo.getDefectCount();
		this.analysis = dpaWorkbookCollectionMongo.getAnalysis();
		this.primaryCause = dpaWorkbookCollectionMongo.getPrimaryCause();
		this.correctiveDescription = dpaWorkbookCollectionMongo.getCorrectiveDescription();
		if(dpaWorkbookCollectionMongo.getCorrectivePlannedStartDate() != null){
			this.correctivePlannedStartDate = (Date) dpaWorkbookCollectionMongo.getCorrectivePlannedStartDate();
		}
		if(dpaWorkbookCollectionMongo.getCorrectivePlannedEndDate() != null){
			this.correctivePlannedEndDate = (Date) dpaWorkbookCollectionMongo.getCorrectivePlannedEndDate();
		} 
		if(dpaWorkbookCollectionMongo.getCorrectiveActualStartDate() != null){
			this.correctiveActualStartDate = (Date) dpaWorkbookCollectionMongo.getCorrectiveActualStartDate();
		}
		if(dpaWorkbookCollectionMongo.getCorrectiveActualEndDate() != null){
			this.correctiveActualEndDate = (Date) dpaWorkbookCollectionMongo.getCorrectiveActualEndDate();
		}
		this.correctiveClosureResponsibility = dpaWorkbookCollectionMongo.getCorrectiveClosureResponsibility();
		this.correctiveActionStatus = dpaWorkbookCollectionMongo.getCorrectiveActionStatus();
		this.correctiveEvaluatingEffectiveness = dpaWorkbookCollectionMongo.getCorrectiveEvaluatingEffectiveness();
		if(dpaWorkbookCollectionMongo.getCorrectiveEvaluatingDate() != null){
			this.correctiveEvaluatingDate = (Date) dpaWorkbookCollectionMongo.getCorrectiveEvaluatingDate();
		}
		this.correctiveEffectiveness = dpaWorkbookCollectionMongo.getCorrectiveEffectiveness();
		this.preventiveDescription = dpaWorkbookCollectionMongo.getPreventiveDescription();
		if(dpaWorkbookCollectionMongo.getPreventivePlannedStartDate() != null){
			this.preventivePlannedStartDate = (Date) dpaWorkbookCollectionMongo.getPreventivePlannedStartDate();
		}
		if(dpaWorkbookCollectionMongo.getPreventivePlannedEndDate() != null){
			this.preventivePlannedEndDate = (Date) dpaWorkbookCollectionMongo.getPreventivePlannedEndDate();
		}
		if(dpaWorkbookCollectionMongo.getPreventiveActualStartDate() != null){
			this.preventiveActualStartDate = (Date) dpaWorkbookCollectionMongo.getPreventiveActualStartDate();
		}
		if(dpaWorkbookCollectionMongo.getPreventiveActualEndDate() != null){
			this.preventiveActualEndDate = (Date) dpaWorkbookCollectionMongo.getPreventiveActualEndDate();
		}
		this.preventiveClosureResponsibility = dpaWorkbookCollectionMongo.getPreventiveClosureResponsibility();
		this.preventiveActionStatus = dpaWorkbookCollectionMongo.getPreventiveActionStatus();
		this.preventiveEvaluatingEffectiveness = dpaWorkbookCollectionMongo.getPreventiveEvaluatingEffectiveness();
		if(dpaWorkbookCollectionMongo.getPreventiveEvaluatingDate() != null){
			this.preventiveEvaluatingDate = (Date) dpaWorkbookCollectionMongo.getPreventiveEvaluatingDate();
		}
		this.preventiveEffectiveness = dpaWorkbookCollectionMongo.getPreventiveEffectiveness();
		this.remarks = dpaWorkbookCollectionMongo.getRemarks();
		if(dpaWorkbookCollectionMongo.getCreatedDate() != null){
			this.createdDate = (Date) dpaWorkbookCollectionMongo.getCreatedDate();
		}
		if(dpaWorkbookCollectionMongo.getUpdateDate() != null){
			this.updateDate = (Date) dpaWorkbookCollectionMongo.getUpdateDate();
		}
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

	public String getDpaId() {
		return dpaId;
	}

	public void setDpaId(String dpaId) {
		this.dpaId = dpaId;
	}

	public Date getMeetingReferenceDate() {
		return meetingReferenceDate;
	}

	public void setMeetingReferenceDate(Date meetingReferenceDate) {
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

	public Date getCorrectivePlannedStartDate() {
		return correctivePlannedStartDate;
	}

	public void setCorrectivePlannedStartDate(Date correctivePlannedStartDate) {
		this.correctivePlannedStartDate = correctivePlannedStartDate;
	}

	public Date getCorrectivePlannedEndDate() {
		return correctivePlannedEndDate;
	}

	public void setCorrectivePlannedEndDate(Date correctivePlannedEndDate) {
		this.correctivePlannedEndDate = correctivePlannedEndDate;
	}

	public Date getCorrectiveActualStartDate() {
		return correctiveActualStartDate;
	}

	public void setCorrectiveActualStartDate(Date correctiveActualStartDate) {
		this.correctiveActualStartDate = correctiveActualStartDate;
	}

	public Date getCorrectiveActualEndDate() {
		return correctiveActualEndDate;
	}

	public void setCorrectiveActualEndDate(Date correctiveActualEndDate) {
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

	public Date getCorrectiveEvaluatingDate() {
		return correctiveEvaluatingDate;
	}

	public void setCorrectiveEvaluatingDate(Date correctiveEvaluatingDate) {
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

	public Date getPreventivePlannedStartDate() {
		return preventivePlannedStartDate;
	}

	public void setPreventivePlannedStartDate(Date preventivePlannedStartDate) {
		this.preventivePlannedStartDate = preventivePlannedStartDate;
	}

	public Date getPreventivePlannedEndDate() {
		return preventivePlannedEndDate;
	}

	public void setPreventivePlannedEndDate(Date preventivePlannedEndDate) {
		this.preventivePlannedEndDate = preventivePlannedEndDate;
	}

	public Date getPreventiveActualStartDate() {
		return preventiveActualStartDate;
	}

	public void setPreventiveActualStartDate(Date preventiveActualStartDate) {
		this.preventiveActualStartDate = preventiveActualStartDate;
	}

	public Date getPreventiveActualEndDate() {
		return preventiveActualEndDate;
	}

	public void setPreventiveActualEndDate(Date preventiveActualEndDate) {
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

	public Date getPreventiveEvaluatingDate() {
		return preventiveEvaluatingDate;
	}

	public void setPreventiveEvaluatingDate(Date preventiveEvaluatingDate) {
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

}
