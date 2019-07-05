package com.hcl.atf.taf.mongodb.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.TestExecutionResultBugList;

@Document(collection = "testcasedefects")
public class TestCaseDefectsMasterMongo {
	private static final Log log = LogFactory.getLog(TestCaseDefectsMasterMongo.class);	
	@Transient
	private SimpleDateFormat dateFormatForMongoDB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
	@Id
	private String id;
	private String _class;
	private Integer bugId;
	private String title;
	private String description;
	
	private Integer testFactoryId;
	private String testFactoryName;
	
	private Integer testCentersId;
	private String testCentersName;
	
	private Integer productId;
	private String productName;
	
	private Integer versionId;
	private String versionName;
	private Integer buildId;
	private String buildName;
	private Integer workPackageId;
	private String workPackageName;
	private String defectType;
	private String req_type;
	private String priority;
	private String severity;
	private String detection;
	private String injection;
	private String internaldefect;
	private String feature;
	
	private String kbid;
	private String status;
	private String resolution;
	private String updated_by;
	private String file;
	private String primary_feature;
	private String primary_feature_parent;
	private String secondary_feature;
	private String mapped;
	private String testcases;
	private String confirmed;
	
	private Integer testCaseExecutionResult;
	private String bugManagementSystemName;
	private String bugManagementSystemBugId;
	private String remarks;
	private String product_name;
	private Integer testRunJobId;
	
	private String test_beds;
	
	private Integer testCaseid;
	private String testCaseName;
	private String testCaseCode;
	private String testCaseType;
	private String testCasePriority;
	private String testCaseSource;
	private String executionType;
	
	private Integer shiftTypeId;
	private String shiftTypeName;
	private Integer workShiftId;
	private Integer workShiftName;
	private Integer actualShiftId;
	private String actualShiftName;

	
	private String productType;

	private Integer customerId;
	private String customerName;
	
	private Integer environmentId;
	private String environment;
	
	private String reporter;
	private String testerName;
	private String testerPriority;

	private String approvalStatus;
	private String approver;
	private String approvalRemarks;
	private Object approvalDate;
	private String approverPriority; 
	private String approverSeverity;
	private String approved;
	private String analysed;
	private String assignee;
	private Integer bugFilingStatusId;
	private String bugFilingStatusName;

	private Object createdDate;
	private Object modifiedDate;
	
	public TestCaseDefectsMasterMongo(){
			
	}
	
	public TestCaseDefectsMasterMongo(TestExecutionResultBugList bug) {
		
		if (bug.getBugManagementSystemBugId() != null)
			this.id = bug.getTestExecutionResultBugId()+"";
		else
			this.id = bug.getTestExecutionResultBugId() + "";
		
		this.bugId = bug.getTestExecutionResultBugId();
		this.title = bug.getBugTitle();
		this.description = bug.getBugDescription();
		if(bug.getTestersPriority()!=null){
			this.priority = bug.getTestersPriority().getExecutionPriorityName(); // ??
		}
	
		if(bug.getDefectSeverity()!=null){
			this.severity = bug.getDefectSeverity().getSeverityName();
		}
		if( bug.getDefectFoundStage()!=null){
			this.detection = bug.getDefectFoundStage().getStageName();
		}
		
		this.createdDate = bug.getBugCreationTime();
		this.status = null; // ??
		this.injection = null;
		this.resolution = null; // ??
		this.kbid = null; // ??
		
		if(bug.getTestCaseExecutionResult()!=null && bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan()!=null){
			if(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration()!=null && bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration()!=null){
				this.test_beds = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getRunconfigName();
				this.environment = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getRunconfigName();
				this.environmentId = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getRunconfigId();
			}
			
			if(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase()!=null){
				this.testCaseid = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseId();
				this.testCaseName = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseName();
				this.testCaseCode = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseCode();
				this.testCaseType = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseType();
				this.testCaseSource = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseSource();
				
				if(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCasePriority()!=null){
					this.testCasePriority = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCasePriority().getPriorityName();	
				}
				
				if(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getExecutionTypeMaster()!=null){
					this.executionType = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getExecutionTypeMaster().getName();
				}
				
				Set<ProductFeature> features = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getProductFeature();
				if (features != null && !features.isEmpty()) {
					Iterator iter = features.iterator();
					ProductFeature feature = (ProductFeature) iter.next();
					this.primary_feature = feature.getProductFeatureName();
					this.req_type = null; //TODO

				}
				
			}
			
			this.secondary_feature = null;
if(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage()!=null){
	this.workPackageId = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getWorkPackageId();
	this.workPackageName = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getName();
	if(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild()!=null){
		this.buildId = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductBuildId();//build Id
		this.buildName = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getBuildname();
		if(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion()!=null){
			this.versionId= bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();//ver Id
			this.versionName = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
		}
		if(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
			this.productId = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
			this.productName = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
			if(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer()!=null){
				this.customerId = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerId();
				this.customerName = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerName();
			}
		}
		if(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory()!=null){
			this.testFactoryId = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId();
			this.testFactoryName = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getDisplayName();
			if(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab()!=null){
				this.testCentersId=bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
				this.testCentersName=bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
			}
		}
		
	}
}
			
			this.testRunJobId = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestRunJob().getTestRunJobId();
			

			if(actualShiftId!=null){
				this.actualShiftId = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getActualWorkShiftMaster().getShiftId();
			}
			if(actualShiftName!=null){
				this.actualShiftName = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getActualWorkShiftMaster().getDisplayLabel();
			}
		
			
			if(shiftTypeId!=null){
				this.shiftTypeId = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getActualWorkShiftMaster().getShiftType().getShiftTypeId();
			}
			
			if(shiftTypeName!=null){
				this.shiftTypeName = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getActualWorkShiftMaster().getShiftType().getDisplayLabel();
			}
			
		if(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTester()!=null){
			this.reporter = bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTester().getLoginId();
			this.testerName= bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTester().getLoginId();
		}
			
		}
		
		
		this.internaldefect = null;
		if( bug.getDefectType()!=null){
			this.defectType = bug.getDefectType().getDefectTypeName();
		}
		
		this.testCaseExecutionResult = bug.getTestCaseExecutionResult().getTestCaseExecutionResultId();
		
		if(bug.getTestersPriority()!=null){
			this.testerPriority = bug.getTestersPriority().getExecutionPriorityName();
		}
		
		if(bug.getDefectApprovalStatus()!=null){
			this.approvalStatus = bug.getDefectApprovalStatus().getApprovalStatusName();
		}
		
		if(bug.getApprovedBy()!=null){
			this.approver = bug.getApprovedBy().getLoginId();
		}
		this.approvalRemarks = bug.getApprovalRemarks();
		this.approvalDate = bug.getApprovedOn();
		
		if(bug.getApproversPriority()!=null){
			this.approverPriority = bug.getApproversPriority().getExecutionPriorityName();
			
		}
	if(bug.getApproversDefectSeverity()!=null){
		this.approverSeverity = bug.getApproversDefectSeverity().getSeverityName();
	}
		
		if (bug.getIsApproved() == 1){
			this.approved = "Yes";
		}else{
			this.approved = "No";
		}
		
		if(bug.getAnalysedFlag()!=null){
			if (bug.getAnalysedFlag() == 1){
				this.analysed = "Yes";
			}else{
				this.analysed = "No";
			}

		}
		
		if(assignee!=null){
			this.assignee = bug.getAssignee().getLoginId();
		}
		if(bug.getModifiedDate()!=null){
			this.modifiedDate=bug.getModifiedDate();
		}
		
		
		
		this.updated_by = null;
		this. file = file; // ??
		this.mapped = null; // ??
		this.confirmed = null; // ??
				
		this. bugManagementSystemName=bugManagementSystemName;
		this. bugManagementSystemBugId=bugManagementSystemBugId;
		this. remarks=remarks;
		
		if(bug.getBugFilingStatus()!=null){
			this.bugFilingStatusId = bug.getBugFilingStatus().getWorkFlowId();
			this.bugFilingStatusName = bug.getBugFilingStatus().getStageName();
		}
		
	}
	
	public TestCaseDefectsMasterMongo(String _id, String title, String description,
			String versionName,String product_name,String workpackage_name,String test_beds, String build, String req_type, String priority, String severity, String detection, String injection, String internaldefect, String feature, Date created_date,
			String kbid, String status, Date updated_date, String resolution, String updated_by, Date last_updated_date, String file, String primary_feature, String primary_feature_parent, String secondary_feature, 
			String mapped, Integer testExecutionResultBugId, Integer testCaseExecutionResult, String bugManagementSystemName, String bugManagementSystemBugId, String remarks,String testcases,String confirmed){
		
		this.id=_id;
		this.title=title;
		this.description=description;
		this.versionName=versionName;
		this.product_name=product_name;
		this.workPackageName=workpackage_name;
		this.test_beds=test_beds;
		this.buildName=buildName;
		this.  req_type=req_type;
		this. priority=priority;
		this. severity=severity;
		this. detection=detection;
		this. injection=injection;
		this. internaldefect=internaldefect;
		this. feature=feature;
		this.  createdDate=created_date;
		this. kbid=kbid;
		this. status=status;
		this. resolution=resolution;
		this. updated_by=updated_by;
		this. file=file;
		this.primary_feature=primary_feature;
		this.primary_feature_parent=primary_feature_parent;
		this.secondary_feature=secondary_feature;
		this.mapped=mapped;
		this.testcases=testcases;
		this.confirmed=confirmed;
				
		
		this. testCaseExecutionResult=testCaseExecutionResult;
		this. bugManagementSystemName=bugManagementSystemName;
		this. bugManagementSystemBugId=bugManagementSystemBugId;
		this. remarks=remarks;
		
		this.bugFilingStatusId = 0;
		this.bugFilingStatusName = "";
	}


	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRelease() {
		return versionName;
	}


	public void setRelease(String versionName) {
		this.versionName = versionName;
	}

	public String getProduct_name() {
		return product_name;
	}


	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}


	public String getWorkPackageName() {
		return workPackageName;
	}


	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}


	

	public String getTest_beds() {
		return test_beds;
	}


	public void setTest_beds(String test_beds) {
		this.test_beds = test_beds;
	}


	


	

	public String getReq_type() {
		return req_type;
	}


	public void setReq_type(String req_type) {
		this.req_type = req_type;
	}
	public String getPriority() {
		return priority;
	}


	public void setPriority(String priority) {
		this.priority = priority;
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

	public String getInternaldefect() {
		return internaldefect;
	}

	public void setInternaldefect(String internaldefect) {
		this.internaldefect = internaldefect;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
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


	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getUpdated_by() {
		return updated_by;
	}


	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getPrimary_feature() {
		return primary_feature;
	}


	public void setPrimary_feature(String primary_feature) {
		this.primary_feature = primary_feature;
	}


	public String getPrimary_feature_parent() {
		return primary_feature_parent;
	}

	public void setPrimary_feature_parent(String primary_feature_parent) {
		this.primary_feature_parent = primary_feature_parent;
	}


	public String getSecondary_feature() {
		return secondary_feature;
	}

	public void setSecondary_feature(String secondary_feature) {
		this.secondary_feature = secondary_feature;
	}

	public String getMapped() {
		return mapped;
	}

	public void setMapped(String mapped) {
		this.mapped = mapped;
	}


	public Integer getTestCaseExecutionResult() {
		return testCaseExecutionResult;
	}


	public void setTestCaseExecutionResult(
			Integer testCaseExecutionResult) {
		this.testCaseExecutionResult = testCaseExecutionResult;
	}

	public String getBugManagementSystemName() {
		return bugManagementSystemName;
	}

	public void setBugManagementSystemName(String bugManagementSystemName) {
		this.bugManagementSystemName = bugManagementSystemName;
	}

	public String getBugManagementSystemBugId() {
		return bugManagementSystemBugId;
	}


	public void setBugManagementSystemBugId(String bugManagementSystemBugId) {
		this.bugManagementSystemBugId = bugManagementSystemBugId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public String getTestcases() {
		return testcases;
	}


	public void setTestcases(String testcases) {
		this.testcases = testcases;
	}


	public String getConfirmed() {
		return confirmed;
	}


	public void setConfirmed(String confirmed) {
		this.confirmed = confirmed;
	}

	public Integer getBugFilingStatusId() {
		return bugFilingStatusId;
	}

	public void setBugFilingStatusId(Integer bugFilingStatusId) {
		this.bugFilingStatusId = bugFilingStatusId;
	}

	public String getBugFilingStatusName() {
		return bugFilingStatusName;
	}

	public void setBugFilingStatusName(String bugFilingStatusName) {
		this.bugFilingStatusName = bugFilingStatusName;
	}

	public Object getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Object modifiedDate) {
		String formatCheck = modifiedDate.toString();
		
		if(formatCheck.contains("date=")){
			try {
				modifiedDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.modifiedDate = modifiedDate;
	}

	public Integer getTestFactoryId() {
		return testFactoryId;
	}

	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}

	public String getTestFactoryName() {
		return testFactoryName;
	}

	public void setTestFactoryName(String testFactoryName) {
		this.testFactoryName = testFactoryName;
	}

	

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public Integer getBuildId() {
		return buildId;
	}

	public void setBuildId(Integer buildId) {
		this.buildId = buildId;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getWorkPackageId() {
		return workPackageId;
	}

	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}

	public Integer getBugId() {
		return bugId;
	}

	public void setBugId(Integer bugId) {
		this.bugId = bugId;
	}

	public String getDefectType() {
		return defectType;
	}

	public void setDefectType(String defectType) {
		this.defectType = defectType;
	}

	public Integer getTestRunJobId() {
		return testRunJobId;
	}

	public void setTestRunJobId(Integer testRunJobId) {
		this.testRunJobId = testRunJobId;
	}

	public Integer getTestCaseid() {
		return testCaseid;
	}

	public void setTestCaseid(Integer testCaseid) {
		this.testCaseid = testCaseid;
	}

	public String getTestCaseCode() {
		return testCaseCode;
	}

	public void setTestCaseCode(String testCaseCode) {
		this.testCaseCode = testCaseCode;
	}

	public String getTestCaseType() {
		return testCaseType;
	}

	public void setTestCaseType(String testCaseType) {
		this.testCaseType = testCaseType;
	}

	public String getTestCasePriority() {
		return testCasePriority;
	}

	public void setTestCasePriority(String testCasePriority) {
		this.testCasePriority = testCasePriority;
	}

	public String getTestCaseSource() {
		return testCaseSource;
	}

	public void setTestCaseSource(String testCaseSource) {
		this.testCaseSource = testCaseSource;
	}

	public String getExecutionType() {
		return executionType;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	public Integer getShiftTypeId() {
		return shiftTypeId;
	}

	public void setShiftTypeId(Integer shiftTypeId) {
		this.shiftTypeId = shiftTypeId;
	}

	public String getShiftTypeName() {
		return shiftTypeName;
	}

	public void setShiftTypeName(String shiftTypeName) {
		this.shiftTypeName = shiftTypeName;
	}

	public Integer getWorkShiftId() {
		return workShiftId;
	}

	public void setWorkShiftId(Integer workShiftId) {
		this.workShiftId = workShiftId;
	}

	public Integer getWorkShiftName() {
		return workShiftName;
	}

	public void setWorkShiftName(Integer workShiftName) {
		this.workShiftName = workShiftName;
	}

	public Integer getActualShiftId() {
		return actualShiftId;
	}

	public void setActualShiftId(Integer actualShiftId) {
		this.actualShiftId = actualShiftId;
	}

	public String getActualShiftName() {
		return actualShiftName;
	}

	public void setActualShiftName(String actualShiftName) {
		this.actualShiftName = actualShiftName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Integer getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getReporter() {
		return reporter;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public String getTesterPriority() {
		return testerPriority;
	}

	public void setTesterPriority(String testerPriority) {
		this.testerPriority = testerPriority;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getApprovalRemarks() {
		return approvalRemarks;
	}

	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}

	public Object getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Object approvalDate) {
		String formatCheck = approvalDate.toString();
		if(formatCheck.contains("date=")){
			try {
				approvalDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.approvalDate = approvalDate;
	}

	public String getApproverPriority() {
		return approverPriority;
	}

	public void setApproverPriority(String approverPriority) {
		this.approverPriority = approverPriority;
	}

	public String getApproverSeverity() {
		return approverSeverity;
	}

	public void setApproverSeverity(String approverSeverity) {
		this.approverSeverity = approverSeverity;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
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

	public String getTesterName() {
		return testerName;
	}

	public void setTesterName(String testerName) {
		this.testerName = testerName;
	}

	public String get_class() {
		return _class;
	}

	public void set_class(String _class) {
		this._class = _class;
	}

	public Integer getTestCentersId() {
		return testCentersId;
	}

	public void setTestCentersId(Integer testCentersId) {
		this.testCentersId = testCentersId;
	}

	public String getTestCentersName() {
		return testCentersName;
	}

	public void setTestCentersName(String testCentersName) {
		this.testCentersName = testCentersName;
	}

	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	public String getAnalysed() {
		return analysed;
	}

	public void setAnalysed(String analysed) {
		this.analysed = analysed;
	}

	
}
