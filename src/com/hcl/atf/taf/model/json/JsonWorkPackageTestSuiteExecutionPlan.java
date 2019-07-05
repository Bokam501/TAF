package com.hcl.atf.taf.model.json;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestSuiteExecutionPlan;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.WorkpackageRunConfiguration;


public class JsonWorkPackageTestSuiteExecutionPlan implements java.io.Serializable{
	
	@JsonProperty	
	private int id;
	
	@JsonProperty	
	private int testSuiteId;
	
	@JsonProperty	
	private String testSuiteName;
	@JsonProperty	
	private String testSuiteDescription;
	
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
	private Integer executionPriorityId;
	
	@JsonProperty	
	private String executionPriorityName;
	@JsonProperty	
	private String executionPriorityDisplayName;
	
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
	private Integer status;
	@JsonProperty	
	private Integer testRunJobId;
	
	public JsonWorkPackageTestSuiteExecutionPlan() {
	}	

	public JsonWorkPackageTestSuiteExecutionPlan(WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlan) {
		try{
		this.id=workPackageTestSuiteExecutionPlan.getId();
		
		this.testSuiteId=workPackageTestSuiteExecutionPlan.getTestsuite().getTestSuiteId();
		this.testSuiteName=workPackageTestSuiteExecutionPlan.getTestsuite().getTestSuiteName();
		this.testSuiteDescription=workPackageTestSuiteExecutionPlan.getTestsuite().getDescription();
		this.workPackageId=workPackageTestSuiteExecutionPlan.getWorkPackage().getWorkPackageId();
		this.workPackageName=workPackageTestSuiteExecutionPlan.getWorkPackage().getName();
		
		this.workpackagePlannedStartDate=DateUtility.sdfDateformatWithOutTime(workPackageTestSuiteExecutionPlan.getWorkPackage().getPlannedStartDate());
		this.workpackagePlannedEndDate=DateUtility.sdfDateformatWithOutTime(workPackageTestSuiteExecutionPlan.getWorkPackage().getPlannedEndDate());
			
	
		if(workPackageTestSuiteExecutionPlan.getRunConfiguration()!=null){
			runConfigurationName=workPackageTestSuiteExecutionPlan.getRunConfiguration().getRunconfiguration().getRunconfigName();
			runConfigurationId=workPackageTestSuiteExecutionPlan.getRunConfiguration().getWorkpackageRunConfigurationId();
			if(runConfigurationName.contains(":")){
				environmentCombinationName=runConfigurationName.substring(0,runConfigurationName.indexOf(":"));
				deviceName=runConfigurationName.substring(runConfigurationName.indexOf(":")+1);
			}else{
				environmentCombinationName=runConfigurationName;
			}
		}

		
		
		if(workPackageTestSuiteExecutionPlan.getTestLead()!=null){
			this.testLeadId =workPackageTestSuiteExecutionPlan.getTestLead().getUserId();
			this.testLeadName =workPackageTestSuiteExecutionPlan.getTestLead().getLoginId();
		}else{
			this.testLeadId=0;
			this.testLeadName=null;
		}
		if(workPackageTestSuiteExecutionPlan.getTester()!=null){
			this.testerId =workPackageTestSuiteExecutionPlan.getTester().getUserId();
			this.testerName =workPackageTestSuiteExecutionPlan.getTester().getLoginId();
		}else{
			this.testerId=0;
			this.testerName=null;
		}
		
		if(workPackageTestSuiteExecutionPlan.getPlannedExecutionDate()!=null){
			this.plannedExecutionDate=DateUtility.sdfDateformatWithOutTime(workPackageTestSuiteExecutionPlan.getPlannedExecutionDate());
		}
		if(workPackageTestSuiteExecutionPlan.getActualExecutionDate()!=null){
			this.actualExecutionDate=DateUtility.sdfDateformatWithOutTime(workPackageTestSuiteExecutionPlan.getActualExecutionDate());
		}
		
		if(workPackageTestSuiteExecutionPlan.getCreatedDate()!=null)
			this.createdDate=DateUtility.dateformatWithOutTime(workPackageTestSuiteExecutionPlan.getCreatedDate());
		if(workPackageTestSuiteExecutionPlan.getModifiedDate()!=null)
			this.modifiedDate=DateUtility.dateformatWithOutTime(workPackageTestSuiteExecutionPlan.getModifiedDate());
		
		if(workPackageTestSuiteExecutionPlan.getPlannedWorkShiftMaster()!=null){
			this.plannedShiftId=workPackageTestSuiteExecutionPlan.getPlannedWorkShiftMaster().getShiftId();
			this.plannedShiftName=workPackageTestSuiteExecutionPlan.getPlannedWorkShiftMaster().getShiftName();
		}
		
		if(workPackageTestSuiteExecutionPlan.getActualWorkShiftMaster()!=null){
			this.actualShiftId=workPackageTestSuiteExecutionPlan.getActualWorkShiftMaster().getShiftId();
			this.actualShiftName=workPackageTestSuiteExecutionPlan.getActualWorkShiftMaster().getShiftName();
		}
		
		if(workPackageTestSuiteExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
			this.productVersionId = workPackageTestSuiteExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();
			this.productVersionName = workPackageTestSuiteExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
		}
		
		if(workPackageTestSuiteExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster() != null){
			this.productId = workPackageTestSuiteExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
			this.productName = workPackageTestSuiteExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
		}
		
		if(workPackageTestSuiteExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode() != null){
			this.productModeId = workPackageTestSuiteExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode().getModeId();
			this.productModeName = workPackageTestSuiteExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode().getModeName();
		}
		
		
		if(workPackageTestSuiteExecutionPlan.getHostList()!=null){
			this.hostId=workPackageTestSuiteExecutionPlan.getHostList().getHostId();
			this.hostName=workPackageTestSuiteExecutionPlan.getHostList().getHostName();
		}
		if(workPackageTestSuiteExecutionPlan.getExecutionPriority()!=null){
			this.executionPriorityId=workPackageTestSuiteExecutionPlan.getExecutionPriority().getExecutionPriorityId();
			this.executionPriorityName=workPackageTestSuiteExecutionPlan.getExecutionPriority().getExecutionPriority();
			this.executionPriorityDisplayName=workPackageTestSuiteExecutionPlan.getExecutionPriority().getDisplayName();
		}
		this.sourceTable="TestSuite";
		this.status=workPackageTestSuiteExecutionPlan.getStatus();
		if(workPackageTestSuiteExecutionPlan.getTestRunJob()!=null){
			this.testRunJobId=workPackageTestSuiteExecutionPlan.getTestRunJob().getTestRunJobId();
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	

	@JsonIgnore
	public WorkPackageTestSuiteExecutionPlan getWorkPackageTestSuiteExecutionPlan() {
		
	
		WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlan = new WorkPackageTestSuiteExecutionPlan();
		workPackageTestSuiteExecutionPlan.setId(this.getId());
		
		
		
		TestSuiteList suite= new TestSuiteList();
		suite.setTestSuiteId(testSuiteId);
		workPackageTestSuiteExecutionPlan.setTestsuite(suite);
		
		WorkPackage workPackage = new WorkPackage();
		workPackage.setWorkPackageId(this.workPackageId);
		workPackageTestSuiteExecutionPlan.setWorkPackage(workPackage);
		
		
		
		WorkpackageRunConfiguration wprc= new WorkpackageRunConfiguration();
		wprc.setWorkpackageRunConfigurationId(runConfigurationId);
		workPackageTestSuiteExecutionPlan.setRunConfiguration(wprc);
		
		UserList testLead = new UserList();
		if(this.testLeadName!=null && !this.testLeadName.equals("")){
			if(isInteger(this.testLeadName)){
				testLead= new UserList();
				testLead.setUserId(new Integer(this.testLeadName));
				workPackageTestSuiteExecutionPlan.setTestLead(testLead);
			}else{
				testLead= new UserList();
				testLead.setFirstName(this.testLeadName);	
				workPackageTestSuiteExecutionPlan.setTestLead(testLead);
			}
		}
				
		
		
		UserList tester =  new UserList();
		if(this.testerName!=null && !this.testerName.equals("")){
			if(isInteger(this.testerName)){
				tester = new UserList();
				tester.setUserId(new Integer(this.testerName));
				workPackageTestSuiteExecutionPlan.setTester(tester);
			}else{
				tester = new UserList();
				tester.setFirstName(this.testerName);
				workPackageTestSuiteExecutionPlan.setTester(tester);
			}
		}
	
		
		if(this.plannedExecutionDate == null || this.plannedExecutionDate.trim().isEmpty()) {
			workPackageTestSuiteExecutionPlan.setPlannedExecutionDate(DateUtility.getCurrentTime());
		} else {
		
			workPackageTestSuiteExecutionPlan.setPlannedExecutionDate(DateUtility.dateformatWithOutTime(plannedExecutionDate));
		}
		
		
		if(this.actualExecutionDate == null || this.actualExecutionDate.trim().isEmpty()) {
			workPackageTestSuiteExecutionPlan.setActualExecutionDate(DateUtility.getCurrentTime());
		} else {
			workPackageTestSuiteExecutionPlan.setActualExecutionDate(DateUtility.dateformatWithOutTime(this.actualExecutionDate));
		}
	
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			workPackageTestSuiteExecutionPlan.setCreatedDate(DateUtility.getCurrentTime());
		} else {
			workPackageTestSuiteExecutionPlan.setCreatedDate(DateUtility.dateformatWithOutTime(this.createdDate));
			
		}
		workPackageTestSuiteExecutionPlan.setModifiedDate(DateUtility.getCurrentTime());
		
		WorkShiftMaster plannedWorkShiftMaster=new WorkShiftMaster();
		if(this.plannedShiftName!=null && !this.plannedShiftName.equals("")){
			if(isInteger(this.plannedShiftName)){
				plannedWorkShiftMaster= new WorkShiftMaster();
				plannedWorkShiftMaster.setShiftId(new Integer(this.plannedShiftName));
				workPackageTestSuiteExecutionPlan.setPlannedWorkShiftMaster(plannedWorkShiftMaster);
			}else{
				plannedWorkShiftMaster= new WorkShiftMaster();
				plannedWorkShiftMaster.setShiftName(this.plannedShiftName);
				workPackageTestSuiteExecutionPlan.setPlannedWorkShiftMaster(plannedWorkShiftMaster);
			}
		}
		
		
		WorkShiftMaster actualWorkShiftMaster=new WorkShiftMaster();
		if(this.actualShiftName!=null && !this.actualShiftName.equals("")){
			if(isInteger(this.actualShiftName)){
				actualWorkShiftMaster= new WorkShiftMaster();
				actualWorkShiftMaster.setShiftId(new Integer(this.actualShiftName));
				workPackageTestSuiteExecutionPlan.setActualWorkShiftMaster(actualWorkShiftMaster);
			}else{
				actualWorkShiftMaster= new WorkShiftMaster();
				actualWorkShiftMaster.setShiftName(this.actualShiftName);
				workPackageTestSuiteExecutionPlan.setActualWorkShiftMaster(actualWorkShiftMaster);
			}
		}
		
		HostList hostList = new HostList();
		if(hostId!=null){
			hostList.setHostId(hostId);
			workPackageTestSuiteExecutionPlan.setHostList(hostList);
		}
		workPackageTestSuiteExecutionPlan.setStatus(status);
		TestRunJob testRunJob = new TestRunJob();
		if(testRunJobId!=null){
			testRunJob.setTestRunJobId(testRunJobId);
			workPackageTestSuiteExecutionPlan.setTestRunJob(testRunJob);	
		}

		return workPackageTestSuiteExecutionPlan;
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

	

	public void setProductModeId(Integer productModeId) {
		this.productModeId = productModeId;
	}

	

	public int getTestSuiteId() {
		return testSuiteId;
	}

	public void setTestSuiteId(int testSuiteId) {
		this.testSuiteId = testSuiteId;
	}

	public String getTestSuiteName() {
		return testSuiteName;
	}

	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}

	public String getTestSuiteDescription() {
		return testSuiteDescription;
	}

	public void setTestSuiteDescription(String testSuiteDescription) {
		this.testSuiteDescription = testSuiteDescription;
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
