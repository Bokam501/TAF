package com.hcl.atf.taf.model.json;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageFeatureExecutionPlan;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.WorkpackageRunConfiguration;


public class JsonWorkPackageFeatureExecutionPlan implements java.io.Serializable{
	
	@JsonProperty	
	private int id;
	
	@JsonProperty	
	private int featureId;
	
	@JsonProperty	
	private String featureName;
	@JsonProperty	
	private String featureDescription;
	@JsonProperty	
	private String featureCode;
	@JsonProperty
	private int workPackageId;
	
	@JsonProperty	
	private String workPackageName;
	
	
	@JsonProperty
	private int testLeadId;
	
	@JsonProperty	
	private String testLeadName;
	
	@JsonProperty
	private int testerId;
	
	@JsonProperty	
	private String testerName;
	
	@JsonProperty	
	private String plannedExecutionDate;
	
	@JsonProperty	
	private String actualExecutionDate;
	
	@JsonProperty	
	private String createdDate;
	
	@JsonProperty	
	private String modifiedDate;
	
	@JsonProperty
	private int plannedShiftId;
	
	@JsonProperty
	private String plannedShiftName;
	
	@JsonProperty
	private int actualShiftId;
	
	@JsonProperty
	private String actualShiftName;
	@JsonProperty	
	private String workpackagePlannedStartDate;
	@JsonProperty	
	private String workpackagePlannedEndDate;
	
	@JsonProperty
	private int productId;
	@JsonProperty	
	private String productName;
	@JsonProperty
	private int productVersionId;
	@JsonProperty	
	private String productVersionName;
	@JsonProperty	
	private String environmentCombinationName;
	
	@JsonProperty	
	private String deviceName;
	
	@JsonProperty	
	private String runConfigurationName;
	@JsonProperty	
	private Integer runConfigurationId;

	
	@JsonProperty	
	private Integer hostId;

	@JsonProperty	
	private String hostName;
	
	@JsonProperty
	private Integer productModeId;
	@JsonProperty	
	private String productModeName;
	@JsonProperty	
	private String sourceTable;
	@JsonProperty
	private Integer executionPriorityId;
	
	@JsonProperty	
	private String executionPriorityName;
	@JsonProperty	
	private String executionPriorityDisplayName;
	@JsonProperty
	private Integer status;
	@JsonProperty	
	private Integer testRunJobId;
	
	public JsonWorkPackageFeatureExecutionPlan() {
	}	

	public JsonWorkPackageFeatureExecutionPlan(WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlan) {
		try{
		this.id=workPackageFeatureExecutionPlan.getId();
		
		this.featureId=workPackageFeatureExecutionPlan.getFeature().getProductFeatureId();
		this.featureName=workPackageFeatureExecutionPlan.getFeature().getProductFeatureName();
		this.featureDescription=workPackageFeatureExecutionPlan.getFeature().getProductFeatureDescription();
		this.featureCode=workPackageFeatureExecutionPlan.getFeature().getProductFeatureCode();
		this.workPackageId=workPackageFeatureExecutionPlan.getWorkPackage().getWorkPackageId();
		this.workPackageName=workPackageFeatureExecutionPlan.getWorkPackage().getName();
		
		this.workpackagePlannedStartDate=DateUtility.sdfDateformatWithOutTime(workPackageFeatureExecutionPlan.getWorkPackage().getPlannedStartDate());
		this.workpackagePlannedEndDate=DateUtility.sdfDateformatWithOutTime(workPackageFeatureExecutionPlan.getWorkPackage().getPlannedEndDate());
			
	
		if(workPackageFeatureExecutionPlan.getRunConfiguration()!=null){
			runConfigurationName=workPackageFeatureExecutionPlan.getRunConfiguration().getRunconfiguration().getRunconfigName();
			runConfigurationId=workPackageFeatureExecutionPlan.getRunConfiguration().getWorkpackageRunConfigurationId();
			if(runConfigurationName.contains(":")){
				environmentCombinationName=runConfigurationName.substring(0,runConfigurationName.indexOf(":"));
				deviceName=runConfigurationName.substring(runConfigurationName.indexOf(":")+1);
			}else{
				environmentCombinationName=runConfigurationName;
			}
		}

		
		
		if(workPackageFeatureExecutionPlan.getTestLead()!=null){
			this.testLeadId =workPackageFeatureExecutionPlan.getTestLead().getUserId();
			this.testLeadName =workPackageFeatureExecutionPlan.getTestLead().getLoginId();
		}else{
			this.testLeadId=0;
			this.testLeadName=null;
		}
		if(workPackageFeatureExecutionPlan.getTester()!=null){
			this.testerId =workPackageFeatureExecutionPlan.getTester().getUserId();
			this.testerName =workPackageFeatureExecutionPlan.getTester().getLoginId();
		}else{
			this.testerId=0;
			this.testerName=null;
		}
		
		if(workPackageFeatureExecutionPlan.getPlannedExecutionDate()!=null){
			this.plannedExecutionDate=DateUtility.sdfDateformatWithOutTime(workPackageFeatureExecutionPlan.getPlannedExecutionDate());
		}
		if(workPackageFeatureExecutionPlan.getActualExecutionDate()!=null){
			this.actualExecutionDate=DateUtility.sdfDateformatWithOutTime(workPackageFeatureExecutionPlan.getActualExecutionDate());
		}
		
		if(workPackageFeatureExecutionPlan.getCreatedDate()!=null)
			this.createdDate=DateUtility.dateformatWithOutTime(workPackageFeatureExecutionPlan.getCreatedDate());
		if(workPackageFeatureExecutionPlan.getModifiedDate()!=null)
			this.modifiedDate=DateUtility.dateformatWithOutTime(workPackageFeatureExecutionPlan.getModifiedDate());
		
		if(workPackageFeatureExecutionPlan.getPlannedWorkShiftMaster()!=null){
			this.plannedShiftId=workPackageFeatureExecutionPlan.getPlannedWorkShiftMaster().getShiftId();
			this.plannedShiftName=workPackageFeatureExecutionPlan.getPlannedWorkShiftMaster().getShiftName();
		}
		
		if(workPackageFeatureExecutionPlan.getActualWorkShiftMaster()!=null){
			this.actualShiftId=workPackageFeatureExecutionPlan.getActualWorkShiftMaster().getShiftId();
			this.actualShiftName=workPackageFeatureExecutionPlan.getActualWorkShiftMaster().getShiftName();
		}
		
		if(workPackageFeatureExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
			this.productVersionId = workPackageFeatureExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();
			this.productVersionName = workPackageFeatureExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
		}
		
		if(workPackageFeatureExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster() != null){
			this.productId = workPackageFeatureExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
			this.productName = workPackageFeatureExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
		}
		
		if(workPackageFeatureExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode() != null){
			this.productModeId = workPackageFeatureExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode().getModeId();
			this.productModeName = workPackageFeatureExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode().getModeName();
		}
		
		if(workPackageFeatureExecutionPlan.getExecutionPriority()!=null){
			this.executionPriorityId=workPackageFeatureExecutionPlan.getExecutionPriority().getExecutionPriorityId();
			this.executionPriorityName=workPackageFeatureExecutionPlan.getExecutionPriority().getExecutionPriority();
			this.executionPriorityDisplayName=workPackageFeatureExecutionPlan.getExecutionPriority().getDisplayName();
		}
		if(workPackageFeatureExecutionPlan.getHostList()!=null){
			this.hostId=workPackageFeatureExecutionPlan.getHostList().getHostId();
			this.hostName=workPackageFeatureExecutionPlan.getHostList().getHostName();
		}
		this.sourceTable="Feature";
		this.status=workPackageFeatureExecutionPlan.getStatus();
		if(workPackageFeatureExecutionPlan.getTestRunJob()!=null){
			this.testRunJobId=workPackageFeatureExecutionPlan.getTestRunJob().getTestRunJobId();
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	

	@JsonIgnore
	public WorkPackageFeatureExecutionPlan getWorkPackageFeatureExecutionPlan() {
		
	
		WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlan = new WorkPackageFeatureExecutionPlan();
		workPackageFeatureExecutionPlan.setId(this.getId());
		
		workPackageFeatureExecutionPlan.setStatus(status);
		
		ProductFeature feature= new ProductFeature();
		feature.setProductFeatureId(featureId);
		workPackageFeatureExecutionPlan.setFeature(feature);
		
		WorkPackage workPackage = new WorkPackage();
		workPackage.setWorkPackageId(this.workPackageId);
		workPackageFeatureExecutionPlan.setWorkPackage(workPackage);
		
		
		
		WorkpackageRunConfiguration wprc= new WorkpackageRunConfiguration();
		wprc.setWorkpackageRunConfigurationId(runConfigurationId);
		workPackageFeatureExecutionPlan.setRunConfiguration(wprc);
		
		UserList testLead = new UserList();
		if(this.testLeadName!=null && !this.testLeadName.equals("")){
			if(isInteger(this.testLeadName)){
				testLead= new UserList();
				testLead.setUserId(new Integer(this.testLeadName));
				workPackageFeatureExecutionPlan.setTestLead(testLead);
			}else{
				testLead= new UserList();
				testLead.setFirstName(this.testLeadName);	
				workPackageFeatureExecutionPlan.setTestLead(testLead);
			}
		}
				
		
		
		UserList tester =  new UserList();
		if(this.testerName!=null && !this.testerName.equals("")){
			if(isInteger(this.testerName)){
				tester = new UserList();
				tester.setUserId(new Integer(this.testerName));
				workPackageFeatureExecutionPlan.setTester(tester);
			}else{
				tester = new UserList();
				tester.setFirstName(this.testerName);
				workPackageFeatureExecutionPlan.setTester(tester);
			}
		}
	
		
		if(this.plannedExecutionDate == null || this.plannedExecutionDate.trim().isEmpty()) {
			workPackageFeatureExecutionPlan.setPlannedExecutionDate(DateUtility.getCurrentTime());
		} else {
		
			workPackageFeatureExecutionPlan.setPlannedExecutionDate(DateUtility.dateformatWithOutTime(plannedExecutionDate));
		}
		
		
		if(this.actualExecutionDate == null || this.actualExecutionDate.trim().isEmpty()) {
			workPackageFeatureExecutionPlan.setActualExecutionDate(DateUtility.getCurrentTime());
		} else {
			workPackageFeatureExecutionPlan.setActualExecutionDate(DateUtility.dateformatWithOutTime(this.actualExecutionDate));
		}
	
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			workPackageFeatureExecutionPlan.setCreatedDate(DateUtility.getCurrentTime());
		} else {
			workPackageFeatureExecutionPlan.setCreatedDate(DateUtility.dateformatWithOutTime(this.createdDate));
			
		}
		workPackageFeatureExecutionPlan.setModifiedDate(DateUtility.getCurrentTime());
		
		WorkShiftMaster plannedWorkShiftMaster=new WorkShiftMaster();
		if(this.plannedShiftName!=null && !this.plannedShiftName.equals("")){
			if(isInteger(this.plannedShiftName)){
				plannedWorkShiftMaster= new WorkShiftMaster();
				plannedWorkShiftMaster.setShiftId(new Integer(this.plannedShiftName));
				workPackageFeatureExecutionPlan.setPlannedWorkShiftMaster(plannedWorkShiftMaster);
			}else{
				plannedWorkShiftMaster= new WorkShiftMaster();
				plannedWorkShiftMaster.setShiftName(this.plannedShiftName);
				workPackageFeatureExecutionPlan.setPlannedWorkShiftMaster(plannedWorkShiftMaster);
			}
		}
		
		
		WorkShiftMaster actualWorkShiftMaster=new WorkShiftMaster();
		if(this.actualShiftName!=null && !this.actualShiftName.equals("")){
			if(isInteger(this.actualShiftName)){
				actualWorkShiftMaster= new WorkShiftMaster();
				actualWorkShiftMaster.setShiftId(new Integer(this.actualShiftName));
				workPackageFeatureExecutionPlan.setActualWorkShiftMaster(actualWorkShiftMaster);
			}else{
				actualWorkShiftMaster= new WorkShiftMaster();
				actualWorkShiftMaster.setShiftName(this.actualShiftName);
				workPackageFeatureExecutionPlan.setActualWorkShiftMaster(actualWorkShiftMaster);
			}
		}
		if(executionPriorityId!=null){
			ExecutionPriority executionPriority=new ExecutionPriority();
			executionPriority.setExecutionPriorityId(executionPriorityId);
			workPackageFeatureExecutionPlan.setExecutionPriority(executionPriority);
		}
		HostList hostList = new HostList();
		if(hostId!=null){
			hostList.setHostId(hostId);
			workPackageFeatureExecutionPlan.setHostList(hostList);
		}
		
		TestRunJob testRunJob = new TestRunJob();
		if(testRunJobId!=null){
			testRunJob.setTestRunJobId(testRunJobId);
			workPackageFeatureExecutionPlan.setTestRunJob(testRunJob);	
		}
		return workPackageFeatureExecutionPlan;
	}
	
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public int getWorkPackageId() {
		return workPackageId;
	}

	public void setWorkPackageId(int workPackageId) {
		this.workPackageId = workPackageId;
	}

	public String getWorkPackageName() {
		return workPackageName;
	}

	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}

	public int getTestLeadId() {
		return testLeadId;
	}

	public void setTestLeadId(int testLeadId) {
		this.testLeadId = testLeadId;
	}

	public String getTestLeadName() {
		return testLeadName;
	}

	public void setTestLeadName(String testLeadName) {
		this.testLeadName = testLeadName;
	}

	public int getTesterId() {
		return testerId;
	}

	public void setTesterId(int testerId) {
		this.testerId = testerId;
	}

	public String getTesterName() {
		return testerName;
	}

	public void setTesterName(String testerName) {
		this.testerName = testerName;
	}

	public String getPlannedExecutionDate() {
		return plannedExecutionDate;
	}

	public void setPlannedExecutionDate(String plannedExecutionDate) {
		this.plannedExecutionDate = plannedExecutionDate;
	}

	public String getActualExecutionDate() {
		return actualExecutionDate;
	}

	public void setActualExecutionDate(String actualExecutionDate) {
		this.actualExecutionDate = actualExecutionDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


	public int getPlannedShiftId() {
		return plannedShiftId;
	}

	public void setPlannedShiftId(int plannedShiftId) {
		this.plannedShiftId = plannedShiftId;
	}

	public String getPlannedShiftName() {
		return plannedShiftName;
	}

	public void setPlannedShiftName(String plannedShiftName) {
		this.plannedShiftName = plannedShiftName;
	}

	public int getActualShiftId() {
		return actualShiftId;
	}

	public void setActualShiftId(int actualShiftId) {
		this.actualShiftId = actualShiftId;
	}

	public String getActualShiftName() {
		return actualShiftName;
	}

	public void setActualShiftName(String actualShiftName) {
		this.actualShiftName = actualShiftName;
	}

	public String getWorkpackagePlannedStartDate() {
		return workpackagePlannedStartDate;
	}

	public void setWorkpackagePlannedStartDate(String workpackagePlannedStartDate) {
		this.workpackagePlannedStartDate = workpackagePlannedStartDate;
	}

	public String getWorkpackagePlannedEndDate() {
		return workpackagePlannedEndDate;
	}

	public void setWorkpackagePlannedEndDate(String workpackagePlannedEndDate) {
		this.workpackagePlannedEndDate = workpackagePlannedEndDate;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductVersionId() {
		return productVersionId;
	}

	public void setProductVersionId(int productVersionId) {
		this.productVersionId = productVersionId;
	}

	public String getProductVersionName() {
		return productVersionName;
	}

	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}

	public String getEnvironmentCombinationName() {
		return environmentCombinationName;
	}

	public void setEnvironmentCombinationName(String environmentCombinationName) {
		this.environmentCombinationName = environmentCombinationName;
	}


	public String getRunConfigurationName() {
		return runConfigurationName;
	}

	public void setRunConfigurationName(String runConfigurationName) {
		this.runConfigurationName = runConfigurationName;
	}

	public Integer getRunConfigurationId() {
		return runConfigurationId;
	}

	public void setRunConfigurationId(Integer runConfigurationId) {
		this.runConfigurationId = runConfigurationId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
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

	public int getProductModeId() {
		return productModeId;
	}

	public void setProductModeId(int productModeId) {
		this.productModeId = productModeId;
	}

	public String getProductModeName() {
		return productModeName;
	}

	public void setProductModeName(String productModeName) {
		this.productModeName = productModeName;
	}

	public int getFeatureId() {
		return featureId;
	}

	public void setFeatureId(int featureId) {
		this.featureId = featureId;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getFeatureCode() {
		return featureCode;
	}

	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}

	public void setProductModeId(Integer productModeId) {
		this.productModeId = productModeId;
	}

	public String getFeatureDescription() {
		return featureDescription;
	}

	public void setFeatureDescription(String featureDescription) {
		this.featureDescription = featureDescription;
	}

	public String getSourceTable() {
		return sourceTable;
	}

	public void setSourceTable(String sourceTable) {
		this.sourceTable = sourceTable;
	}

	public Integer getExecutionPriorityId() {
		return executionPriorityId;
	}

	public void setExecutionPriorityId(Integer executionPriorityId) {
		this.executionPriorityId = executionPriorityId;
	}

	public String getExecutionPriorityName() {
		return executionPriorityName;
	}

	public void setExecutionPriorityName(String executionPriorityName) {
		this.executionPriorityName = executionPriorityName;
	}

	public String getExecutionPriorityDisplayName() {
		return executionPriorityDisplayName;
	}

	public void setExecutionPriorityDisplayName(String executionPriorityDisplayName) {
		this.executionPriorityDisplayName = executionPriorityDisplayName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getTestRunJobId() {
		return testRunJobId;
	}

	public void setTestRunJobId(Integer testRunJobId) {
		this.testRunJobId = testRunJobId;
	}
	
	
}
