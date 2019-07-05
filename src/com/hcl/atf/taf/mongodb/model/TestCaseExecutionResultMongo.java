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
import com.hcl.atf.taf.model.TestCaseExecutionResult;

@Document(collection = "testexecutions")
public class TestCaseExecutionResultMongo {
	private static final Log log = LogFactory.getLog(TestCaseExecutionResultMongo.class);
	@Transient
	private SimpleDateFormat dateFormatForMongoDB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
	
	@Id
	private String id;	
	private String _class;
	private Integer testCaseExecutionResultId;
	
	private Integer versionId;
	private String versionName;
	
	private Integer buildId;
	private String buildName;
	
	private Integer productId;
	private String productName;
	
	private Integer testFactoryId;
	private String testFactoryName;
	
	
	private Integer testCentersId;
	private String testCentersName;
	
	
	private Integer customerId;
	private String customerName;
	
	
	private Integer testcaseId;
	private String testcaseName;
	
	private String title;
	private String environment;
	private String status;//passed/failed
	private Object executionDate;//execution date
	private Object planneddate;
	private String executionid;
	private String category;
	private String updated_by;
	private String rag_status;//R
	private String value;
	private String featureName;
	private String primary_feature_parent;	
	private String result;
	private Integer workPackageTestCaseExecutionPlanId;
	private Integer defectsCount;
	private String defectIds;
	private String comments;
	private String approved;
	private String reviewed;
	private String observedOutput;
	private Long executionTime;
	private String testExecutionResultBugListSet;	
	
	//New Attributes
	private String testCaseCode;
	private String testCaseType;
	private String testCasePriority;
	private String testCaseSource;
	
	private String executionType;
	private Integer decouplingCategoryId;
	private String decouplingCategoryName;
	private Integer plannedTestStepsCount;
	private Integer actualTestStepCount;
	private Integer testSuiteId;
	private String testSuiteName;

	private Integer testRunJobId;
	private Integer workPackageId;
	private String workPackageName;
	
	private Integer jobId;
	private String jobName;
	private Integer testerId;
	private String testerName;
	private Integer testLeadId;
	private String testLeadName;
	private Integer testerResourcePoolId;
	private String testerResourcePoolName;
	private Integer shiftTypeId;
	private String shiftTypeName;
	private Integer workShiftId;
	private Integer workShiftName;
	private Integer actualShiftId;
	private String actualShiftName;
	private String executionPriority;
	private Object executionStartTime;
	private Object executionEndTime;
	private String failureReason;
	private Integer environmentId;
	private String environmentName;
	private Integer hostId;
	private String hostName;
	private String executionStatus;
	private String parentStatus;
	
	private Object createdDate;
	private Object modifiedDate;
	
	//Added to capturing the Mobile Device characteristics
	private String deviceName;
	private Integer platformTypeId;
	private String platformTypeName;
	private String platformTypeVersion;
	
	private String UDID;
	private String deviceModelId;
	private String deviceModel;
	private String kernelNumber;
	private String deviceBuildNumber;
	private Integer deviceMakeId;
	private String deviceMake;
	private Integer screenResolutionX;
	private Integer screenResolutionY;

	public TestCaseExecutionResultMongo(){
		
	}
	
	public TestCaseExecutionResultMongo(String release, String build, String testcaseid, String title, String environment,
			String status, Date date, Date planneddate, String executionid, String category, String last_updated_date,
			String updated_by, String rag_status, String value, String primary_feature, String primary_feature_parent,
			String result, Integer workPackageTestCaseExecutionPlanId,Integer defectsCount, String defectIds, String comments,
			Integer isApproved,Integer isReviewed, String observedOutput, Long executionTime, String testExecutionResultBugListSet,
			String deviceName,String platformTypeName,String platformTypeVersion,String UDID,String deviceModel,String kernelNumber,
			String deviceBuildNumber,String deviceMake,Integer screenResolutionX,Integer screenResolutionY){
		this.versionId = versionId;//ver Id
		this.buildId = buildId;//build Id
		this.testcaseId = testcaseId;
		this.title = title;
		this.environment = environment;
		this.status = status;//passed/failed
		this.executionDate = date;//execution date
		this.planneddate = planneddate;
		this.executionid = executionid;
		this.category = category;
		this.updated_by = updated_by;
		this.rag_status = rag_status;//R
		this.value = value;
		this.featureName = primary_feature;
		this.primary_feature_parent = primary_feature_parent;	
		this.result = result;
		this.workPackageTestCaseExecutionPlanId = workPackageTestCaseExecutionPlanId;
		this.defectsCount = defectsCount;
		this.defectIds = defectIds;
		this.comments = comments;
		this.approved = isApproved+"";
		this.reviewed = isReviewed+"";
		this.observedOutput = observedOutput;
		this.executionTime =executionTime ;
		this.testExecutionResultBugListSet = testExecutionResultBugListSet;		
		
		
		
		
	}	
	
	public TestCaseExecutionResultMongo(TestCaseExecutionResult testCaseResult) {
		
		if(testCaseResult!=null){
			this.id = testCaseResult.getTestCaseExecutionResultId()+"";
			this.testCaseExecutionResultId = testCaseResult.getTestCaseExecutionResultId();
			if(testCaseResult.getTestStepExecutionResultSet()!=null){
				this.actualTestStepCount = testCaseResult.getTestStepExecutionResultSet().size();
				this.defectsCount = testCaseResult.getTestExecutionResultBugListSet().size();
			}
			
			this.defectIds = testCaseResult.getDefectIds();
			this.observedOutput = testCaseResult.getObservedOutput();
			this.comments = testCaseResult.getComments();
			this.failureReason = testCaseResult.getFailureReason();
			this.executionid = testCaseResult.getTestCaseExecutionResultId() + "";
			this.result = testCaseResult.getResult();
			this.executionStartTime = testCaseResult.getStartTime();
			this.executionEndTime = testCaseResult.getEndTime();
			this.createdDate = testCaseResult.getStartTime();
			this.modifiedDate = testCaseResult.getEndTime();
			if (testCaseResult.getIsApproved()==1){
				this.approved = "Yes";
			}
			else{
				this.approved = "No";
			}
			
			if (testCaseResult.getIsReviewed()==1){
				this.reviewed = "Yes";
			}
			else{
				this.reviewed = "No";
			}
			
			
			
			if(testCaseResult.getEndTime()!=null && testCaseResult.getStartTime()!=null)
				this.executionTime = (testCaseResult.getEndTime().getTime() - testCaseResult.getStartTime().getTime()); // TODO : Verify
			
			if(testCaseResult.getWorkPackageTestCaseExecutionPlan()!=null){
				this.workPackageTestCaseExecutionPlanId = testCaseResult.getWorkPackageTestCaseExecutionPlan().getId();
				this.executionDate = testCaseResult.getWorkPackageTestCaseExecutionPlan().getActualExecutionDate();//execution date
				this.planneddate = testCaseResult.getWorkPackageTestCaseExecutionPlan().getPlannedExecutionDate();
				if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getStatus()==1){
					this.status="Active";
					this.parentStatus = "Active";
				}else{
					this.status="InActive";
					this.parentStatus = "Active";
				}
				if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestRunJob()!=null){
					this.testRunJobId = testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestRunJob().getTestRunJobId();
				}
				
				if (testCaseResult.getWorkPackageTestCaseExecutionPlan().getExecutionStatus()==1){
					this.executionStatus = "Assigned";
				}
				else if (testCaseResult.getWorkPackageTestCaseExecutionPlan().getExecutionStatus()==2){
					this.executionStatus = "Completed";
				}else {
					this.executionStatus = "NotStarted ";
				}
				
				if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getActualWorkShiftMaster()!=null){
					this.actualShiftId = testCaseResult.getWorkPackageTestCaseExecutionPlan().getActualWorkShiftMaster().getShiftId();
					this.actualShiftName = testCaseResult.getWorkPackageTestCaseExecutionPlan().getActualWorkShiftMaster().getDisplayLabel();
				
					this.shiftTypeId = testCaseResult.getWorkPackageTestCaseExecutionPlan().getActualWorkShiftMaster().getShiftType().getShiftTypeId();
					this.shiftTypeName = testCaseResult.getWorkPackageTestCaseExecutionPlan().getActualWorkShiftMaster().getShiftType().getDisplayLabel();
				}
				
				
				if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getTester()!=null){
					this.testerId = testCaseResult.getWorkPackageTestCaseExecutionPlan().getTester().getUserId();
					this.testerName = testCaseResult.getWorkPackageTestCaseExecutionPlan().getTester().getLoginId();
				}
				
				if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestLead()!=null){
					this.testLeadId = testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestLead().getUserId();
					this.testLeadName = testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestLead().getLoginId();
				}
				
				
				if( testCaseResult.getWorkPackageTestCaseExecutionPlan().getExecutionPriority()!=null){
					this.executionPriority = testCaseResult.getWorkPackageTestCaseExecutionPlan().getExecutionPriority().getExecutionPriority();
				}
				
				if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getHostList()!=null){
					this.hostId=testCaseResult.getWorkPackageTestCaseExecutionPlan().getHostList().getHostId();
					this.hostName=testCaseResult.getWorkPackageTestCaseExecutionPlan().getHostList().getHostName();
				}
				if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration()!=null){
					if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration()!=null){
						this.environment = testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getRunconfigName();
						this.environmentId = testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getRunconfigId();
						this.environmentName = testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getRunconfigName();
					}
					
					
					if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice() != null){
						
						UDID=testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getUDID();
						screenResolutionX=testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getScreenResolutionX();
						screenResolutionY=testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getScreenResolutionY();
						if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getDeviceModelMaster()!= null) {
							deviceModel=testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getDeviceModelMaster().getDeviceModel();
							deviceName=testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getDeviceModelMaster().getDeviceName();
							if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getDeviceModelMaster().getDeviceMakeMaster() != null) {
								deviceMake = testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getDeviceModelMaster().getDeviceMakeMaster().getDeviceMake();
							}
							if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getPlatformType() != null) {
								
								platformTypeName=testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getPlatformType().getName();
								platformTypeVersion=testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getPlatformType().getVersion();
							}
						}
					}
					
					if(deviceMake != null && !deviceMake.isEmpty()) {
						environment = environment+"~"+deviceMake;
					}
				}
			
				
				
				if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage()!=null){
					this.workPackageId = testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getWorkPackageId();
					this.workPackageName = testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getName();
					if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild()!=null){
						this.buildId = testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductBuildId();//build Id
						this.buildName = testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getBuildname() + "";
						
						if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion()!=null){
							this.versionId = testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductVersionListId() ;
							this.versionName = testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
							
							if( testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
								this.productId = testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
								this.productName = testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
								
								if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory()!=null){
									this.testFactoryId = testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId();
									this.testFactoryName = testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getDisplayName();
								}
								
								if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer()!=null){
									this.customerId = testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerId();
									this.customerName = testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerName();
								}
								if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory()!=null){
									this.testCentersId=testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
									this.testCentersName=testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
								}
							}
							
						}
						
					}
				}
				
				
				if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase()!=null){
					this.testcaseId = testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseId();
					this.testcaseName=testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseName();
					this.testCaseCode = testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseCode();
					this.title = testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseName();
					this.testCaseType = testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseType();
					this.testCaseType = testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseType();
					this.testCaseSource = testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseSource();

					Set<ProductFeature> features = testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase().getProductFeature();
					if (features != null && !features.isEmpty()) {
						Iterator iter = features.iterator();
						ProductFeature feature = (ProductFeature) iter.next();
						this.featureName = feature.getProductFeatureName();
					}
					
					if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCasePriority()!=null){
						this.testCasePriority = testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCasePriority().getPriorityName();
					}
					
					if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase().getExecutionTypeMaster()!=null){
						this.executionType = testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase().getExecutionTypeMaster().getName();
					}
					if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseStepsLists()!=null){
						this.plannedTestStepsCount = testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseStepsLists().size();
					}
					
					
					
				}
				
				
			}
			
		}
		
	}
	

	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
	}

	public Integer getTestCaseExecutionResultId() {
		return testCaseExecutionResultId;
	}

	public void setTestCaseExecutionResultId(Integer testCaseExecutionResultId) {
		this.testCaseExecutionResultId = testCaseExecutionResultId;
	}

	

	

	public Integer getTestcaseId() {
		return testcaseId;
	}

	public void setTestcaseId(Integer testcaseId) {
		this.testcaseId = testcaseId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public Object getPlanneddate() {
		return planneddate;
	}

	public void setPlanneddate(Object planneddate) {
		String formatCheck = planneddate.toString();
		if(formatCheck.contains("date=")){
			try {
				planneddate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.planneddate = planneddate;
	}

	public String getExecutionid() {
		return executionid;
	}

	public void setExecutionid(String executionid) {
		this.executionid = executionid;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}


	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	public String getRag_status() {
		return rag_status;
	}

	public void setRag_status(String rag_status) {
		this.rag_status = rag_status;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	

	public String getPrimary_feature_parent() {
		return primary_feature_parent;
	}

	public void setPrimary_feature_parent(String primary_feature_parent) {
		this.primary_feature_parent = primary_feature_parent;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getWorkPackageTestCaseExecutionPlanId() {
		return workPackageTestCaseExecutionPlanId;
	}

	public void setWorkPackageTestCaseExecutionPlanId(Integer workPackageTestCaseExecutionPlanId) {
		this.workPackageTestCaseExecutionPlanId = workPackageTestCaseExecutionPlanId;
	}

	public Integer getDefectsCount() {
		return defectsCount;
	}

	public void setDefectsCount(Integer defectsCount) {
		this.defectsCount = defectsCount;
	}

	public String getDefectIds() {
		return defectIds;
	}

	public void setDefectIds(String defectIds) {
		this.defectIds = defectIds;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	

	public String getObservedOutput() {
		return observedOutput;
	}

	public void setObservedOutput(String observedOutput) {
		this.observedOutput = observedOutput;
	}

	public Long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}

	public String getTestExecutionResultBugListSet() {
		return testExecutionResultBugListSet;
	}

	public void setTestExecutionResultBugListSet(String testExecutionResultBugListSet) {
		this.testExecutionResultBugListSet = testExecutionResultBugListSet;
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

	public Integer getDecouplingCategoryId() {
		return decouplingCategoryId;
	}

	public void setDecouplingCategoryId(Integer decouplingCategoryId) {
		this.decouplingCategoryId = decouplingCategoryId;
	}

	public String getDecouplingCategoryName() {
		return decouplingCategoryName;
	}

	public void setDecouplingCategoryName(String decouplingCategoryName) {
		this.decouplingCategoryName = decouplingCategoryName;
	}

	public Integer getPlannedTestStepsCount() {
		return plannedTestStepsCount;
	}

	public void setPlannedTestStepsCount(Integer plannedTestStepsCount) {
		this.plannedTestStepsCount = plannedTestStepsCount;
	}

	public Integer getActualTestStepCount() {
		return actualTestStepCount;
	}

	public void setActualTestStepCount(Integer actualTestStepCount) {
		this.actualTestStepCount = actualTestStepCount;
	}

	public Integer getTestSuiteId() {
		return testSuiteId;
	}

	public void setTestSuiteId(Integer testSuiteId) {
		this.testSuiteId = testSuiteId;
	}

	public String getTestSuiteName() {
		return testSuiteName;
	}

	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}

	public Integer getTestRunJobId() {
		return testRunJobId;
	}

	public void setTestRunJobId(Integer testRunJobId) {
		this.testRunJobId = testRunJobId;
	}

	public Integer getWorkPackageId() {
		return workPackageId;
	}

	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}

	public String getWorkPackageName() {
		return workPackageName;
	}

	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
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

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Integer getTesterId() {
		return testerId;
	}

	public void setTesterId(Integer testerId) {
		this.testerId = testerId;
	}

	public String getTesterName() {
		return testerName;
	}

	public void setTesterName(String testerName) {
		this.testerName = testerName;
	}

	public Integer getTestLeadId() {
		return testLeadId;
	}

	public void setTestLeadId(Integer testLeadId) {
		this.testLeadId = testLeadId;
	}

	public String getTestLeadName() {
		return testLeadName;
	}

	public void setTestLeadName(String testLeadName) {
		this.testLeadName = testLeadName;
	}

	public Integer getTesterResourcePoolId() {
		return testerResourcePoolId;
	}

	public void setTesterResourcePoolId(Integer testerResourcePoolId) {
		this.testerResourcePoolId = testerResourcePoolId;
	}

	public String getTesterResourcePoolName() {
		return testerResourcePoolName;
	}

	public void setTesterResourcePoolName(String testerResourcePoolName) {
		this.testerResourcePoolName = testerResourcePoolName;
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

	public String getExecutionPriority() {
		return executionPriority;
	}

	public void setExecutionPriority(String executionPriority) {
		this.executionPriority = executionPriority;
	}

	public Object getExecutionStartTime() {
		return executionStartTime;
	}

	public void setExecutionStartTime(Object executionStartTime) {
		String formatCheck = executionStartTime.toString();
		
		if(formatCheck.contains("date=")){
			try {
				executionStartTime = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.executionStartTime = executionStartTime;
	}

	public Object getExecutionEndTime() {
		return executionEndTime;
	}

	public void setExecutionEndTime(Object executionEndTime) {
		String formatCheck = executionEndTime.toString();
		
		if(formatCheck.contains("date=")){
			try {
				executionEndTime = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.executionEndTime = executionEndTime;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	public Integer getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
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

	@Override
    public String toString(){
	return versionId+":"+buildId+":"+testcaseId+":"+title+":"+environment+":"+status+":"+ executionDate+":"+
			planneddate+":"+executionid+":"+category+":"+":"+updated_by+":"+rag_status+":"+value;
	}

	public Integer getBuildId() {
		return buildId;
	}

	public void setBuildId(Integer buildId) {
		this.buildId = buildId;
	}

	public String getTestcaseName() {
		return testcaseName;
	}

	public void setTestcaseName(String testcaseName) {
		this.testcaseName = testcaseName;
	}

	public Object getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(Object executionDate) {
		String formatCheck = executionDate.toString();
		if(formatCheck.contains("date=")){
			try {
				executionDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.executionDate = executionDate;
	}

	
	

	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	public Integer getHostId() {
		return hostId;
	}

	public void setHostId(Integer hostId) {
		this.hostId = hostId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
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

	public String getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(String executionStatus) {
		this.executionStatus = executionStatus;
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

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	public String getReviewed() {
		return reviewed;
	}

	public void setReviewed(String reviewed) {
		this.reviewed = reviewed;
	}

	public String getParentStatus() {
		return parentStatus;
	}

	public void setParentStatus(String parentStatus) {
		this.parentStatus = parentStatus;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	
	public String getUDID() {
		return UDID;
	}

	public void setUDID(String uDID) {
		UDID = uDID;
	}

	

	public String getKernelNumber() {
		return kernelNumber;
	}

	public void setKernelNumber(String kernelNumber) {
		this.kernelNumber = kernelNumber;
	}

	

	public Integer getScreenResolutionX() {
		return screenResolutionX;
	}

	public void setScreenResolutionX(Integer screenResolutionX) {
		this.screenResolutionX = screenResolutionX;
	}

	public Integer getScreenResolutionY() {
		return screenResolutionY;
	}

	public void setScreenResolutionY(Integer screenResolutionY) {
		this.screenResolutionY = screenResolutionY;
	}

	public Integer getPlatformTypeId() {
		return platformTypeId;
	}

	public String getPlatformTypeName() {
		return platformTypeName;
	}

	public String getPlatformTypeVersion() {
		return platformTypeVersion;
	}

	public String getDeviceModelId() {
		return deviceModelId;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public String getDeviceBuildNumber() {
		return deviceBuildNumber;
	}

	public Integer getDeviceMakeId() {
		return deviceMakeId;
	}

	public String getDeviceMake() {
		return deviceMake;
	}

	public void setPlatformTypeId(Integer platformTypeId) {
		this.platformTypeId = platformTypeId;
	}

	public void setPlatformTypeName(String platformTypeName) {
		this.platformTypeName = platformTypeName;
	}

	public void setPlatformTypeVersion(String platformTypeVersion) {
		this.platformTypeVersion = platformTypeVersion;
	}

	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public void setDeviceBuildNumber(String deviceBuildNumber) {
		this.deviceBuildNumber = deviceBuildNumber;
	}

	public void setDeviceMakeId(Integer deviceMakeId) {
		this.deviceMakeId = deviceMakeId;
	}

	public void setDeviceMake(String deviceMake) {
		this.deviceMake = deviceMake;
	}

	
	
}
