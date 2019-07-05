package com.hcl.atf.taf.model.json;



import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DevicePlatformMaster;
import com.hcl.atf.taf.model.DevicePlatformVersionListMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TestCategoryMaster;
import com.hcl.atf.taf.model.TestEnviromentMaster;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunConfigurationParent;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestToolMaster;


public class JsonTestRunConfigurationChild implements java.io.Serializable {

	@JsonProperty
	private Integer testRunConfigurationChildId;
	@JsonProperty
	private Integer testRunConfigurationParentId;
	
	@JsonProperty
	private Integer productVersionListId;
	@JsonProperty
	private String productVersionName;
	
	
	@JsonProperty
	private Integer testSuiteId;
	@JsonProperty
	private String testSuiteName;
	@JsonProperty
	private String locale;
	
	
	@JsonProperty
	private Integer testEnviromentId;
	@JsonProperty
	private String testEnviromentToolName;
	
	@JsonProperty
	private String testRunScheduledStartTime;
	@JsonProperty	
	private int testRunScheduledIntervalInHour;
	@JsonProperty
	private int testRunRecurrenceLimit;
	
	@JsonProperty
	private String testCategory;
	
	
	@JsonProperty
	private Integer devicePlatformVersionListId;
	@JsonProperty
	private String devicePlatformVersion;
	@JsonProperty
	private String devicePlatformName;
	@JsonProperty
	private int lastRunNo;
	


	@JsonProperty
	private String testRunCronSchedule;
	@JsonProperty
	private String description;
	@JsonProperty
	private String testRunConfigurationName;
	@JsonProperty
	private String testRunScheduledEndTime;
	@JsonProperty
	private String notifyByMail;


	public int getLastRunNo() {
		return lastRunNo;
	}


	public void setLastRunNo(int lastRunNo) {
		this.lastRunNo = lastRunNo;
	}


	

	public JsonTestRunConfigurationChild() {
	}


	public JsonTestRunConfigurationChild(TestRunConfigurationChild testRunConfigurationChild) {
		
		this.testRunConfigurationChildId = testRunConfigurationChild.getTestRunConfigurationChildId();
		this.testRunConfigurationParentId = testRunConfigurationChild.getTestRunConfigurationParent().getTestRunConfigurationParentId();
		if(testRunConfigurationChild.getProductVersionListMaster()!=null){
			this.productVersionListId = testRunConfigurationChild.getProductVersionListMaster().getProductVersionListId();
			this.productVersionName = testRunConfigurationChild.getProductVersionListMaster().getProductVersionName();
		}
		
		if(testRunConfigurationChild.getTestSuiteList()!=null){
			this.testSuiteId = testRunConfigurationChild.getTestSuiteList().getTestSuiteId();
			this.testSuiteName = testRunConfigurationChild.getTestSuiteList().getTestSuiteName();
		}
		if(testRunConfigurationChild.getTestEnviromentMaster()!=null){
			this.testEnviromentId = testRunConfigurationChild.getTestEnviromentMaster().getTestEnvironmentId();
			if(testRunConfigurationChild.getTestEnviromentMaster().getTestToolMaster()!=null){
				this.testEnviromentToolName = testRunConfigurationChild.getTestEnviromentMaster().getTestToolMaster().getTestToolName();
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(testRunConfigurationChild.getTestRunScheduledStartTime()!=null){				
		this.testRunScheduledStartTime = sdf.format(testRunConfigurationChild.getTestRunScheduledStartTime());
		}
		this.testRunScheduledIntervalInHour = testRunConfigurationChild.getTestRunScheduledIntervalInHour();
		this.testRunRecurrenceLimit = testRunConfigurationChild.getTestRunRecurrenceLimit();
		this.lastRunNo=testRunConfigurationChild.getLastRunNo();
		if(testRunConfigurationChild.getTestCategoryMaster()!=null){
			this.testCategory = testRunConfigurationChild.getTestCategoryMaster().getTestCategory();
		}
		
		this.testRunCronSchedule = testRunConfigurationChild.getTestRunCronSchedule();
		this.testRunConfigurationName = testRunConfigurationChild.getTestRunConfigurationName();
		this.description = testRunConfigurationChild.getDescription();
		if(testRunConfigurationChild.getTestRunScheduledEndTime()!=null)
		this.testRunScheduledEndTime = sdf.format(testRunConfigurationChild.getTestRunScheduledEndTime());
		this.notifyByMail = testRunConfigurationChild.getNotifyByMail();
		this.locale = testRunConfigurationChild.getLocale();
		
	}


	public Integer getTestRunConfigurationChildId() {
		return testRunConfigurationChildId;
	}


	public void setTestRunConfigurationChildId(Integer testRunConfigurationChildId) {
		this.testRunConfigurationChildId = testRunConfigurationChildId;
	}


	public Integer getTestRunConfigurationParentId() {
		return testRunConfigurationParentId;
	}


	public void setTestRunConfigurationParentId(Integer testRunConfigurationParentId) {
		this.testRunConfigurationParentId = testRunConfigurationParentId;
	}


	public Integer getProductVersionListId() {
		return productVersionListId;
	}


	public void setProductVersionListId(Integer productVersionListId) {
		this.productVersionListId = productVersionListId;
	}


	public String getProductVersionName() {
		return productVersionName;
	}


	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
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


	public String getLocale() {
		return locale;
	}


	public void setLocale(String locale) {
		this.locale = locale;
	}


	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}


	public Integer getTestEnviromentId() {
		return testEnviromentId;
	}


	public void setTestEnviromentId(Integer testEnviromentId) {
		this.testEnviromentId = testEnviromentId;
	}


	public String getTestEnviromentToolName() {
		return testEnviromentToolName;
	}


	public void setTestEnviromentToolName(String testEnviromentToolName) {
		this.testEnviromentToolName = testEnviromentToolName;
	}


	public String getTestRunScheduledStartTime() {
		return testRunScheduledStartTime;
	}


	public void setTestRunScheduledStartTime(String testRunScheduledStartTime) {
		this.testRunScheduledStartTime = testRunScheduledStartTime;
	}


	public int getTestRunScheduledIntervalInHour() {
		return testRunScheduledIntervalInHour;
	}


	public void setTestRunScheduledIntervalInHour(int testRunScheduledIntervalInHour) {
		this.testRunScheduledIntervalInHour = testRunScheduledIntervalInHour;
	}


	public int getTestRunRecurrenceLimit() {
		return testRunRecurrenceLimit;
	}


	public void setTestRunRecurrenceLimit(int testRunRecurrenceLimit) {
		this.testRunRecurrenceLimit = testRunRecurrenceLimit;
	}


	public String getTestCategory() {
		return testCategory;
	}


	public void setTestCategory(String testCategory) {
		this.testCategory = testCategory;
	}

	
	public Integer getDevicePlatformVersionListId() {
		return devicePlatformVersionListId;
	}


	public void setDevicePlatformVersionListId(Integer devicePlatformVersionListId) {
		this.devicePlatformVersionListId = devicePlatformVersionListId;
	}


	public String getDevicePlatformVersion() {
		return devicePlatformVersion;
	}


	public void setDevicePlatformVersion(String devicePlatformVersion) {
		this.devicePlatformVersion = devicePlatformVersion;
	}


	public String getDevicePlatformName() {
		return devicePlatformName;
	}


	public void setDevicePlatformName(String devicePlatformName) {
		this.devicePlatformName = devicePlatformName;
	}

	
	public String getTestRunCronSchedule() {
		return testRunCronSchedule;
	}


	public void setTestRunCronSchedule(String testRunCronSchedule) {
		this.testRunCronSchedule = testRunCronSchedule;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getTestRunConfigurationName() {
		return testRunConfigurationName;
	}


	public void setTestRunConfigurationName(String testRunConfigurationName) {
		this.testRunConfigurationName = testRunConfigurationName;
	}


	public String getTestRunScheduledEndTime() {
		return testRunScheduledEndTime;
	}


	public void setTestRunScheduledEndTime(String testRunScheduledEndTime) {
		this.testRunScheduledEndTime = testRunScheduledEndTime;
	}

	public String getNotifyByMail() {
		return notifyByMail;
	}

	public void setNotifyByMail(String notifyByMail) {
		this.notifyByMail = notifyByMail;
	}



	@JsonIgnore
	public TestRunConfigurationChild getTestRunConfigurationChild(){
		TestRunConfigurationChild testRunConfigurationChild= new TestRunConfigurationChild();
		testRunConfigurationChild.setTestRunConfigurationChildId(testRunConfigurationChildId);
		
		TestRunConfigurationParent testRunConfigurationParent = new TestRunConfigurationParent();
		if(testRunConfigurationParentId!=null){
			testRunConfigurationParent.setTestRunConfigurationParentId(testRunConfigurationParentId);
		}
		testRunConfigurationChild.setTestRunConfigurationParent(testRunConfigurationParent);
		
		ProductVersionListMaster productVersionListMaster = new ProductVersionListMaster();
		if(productVersionListId!=null){
			productVersionListMaster.setProductVersionListId(productVersionListId);
		}
		DevicePlatformVersionListMaster devicePlatformVersionListMaster = new DevicePlatformVersionListMaster();
		if(devicePlatformVersionListId!=null){
			devicePlatformVersionListMaster.setDevicePlatformVersionListId(devicePlatformVersionListId);
		}
		DevicePlatformMaster devicePlatformMaster = new DevicePlatformMaster();
		if(devicePlatformName!=null){
			devicePlatformMaster.setDevicePlatformName(devicePlatformName);
		}
		devicePlatformVersionListMaster.setDevicePlatformMaster(devicePlatformMaster);
		TestSuiteList testSuiteList = new TestSuiteList();
		if(testSuiteId!=null){
			testSuiteList.setTestSuiteId(testSuiteId);
		}
		testRunConfigurationChild.setTestSuiteList(testSuiteList);
		
		TestEnviromentMaster testEnviromentMaster = new TestEnviromentMaster();
		if(testEnviromentId!=null){
			testEnviromentMaster.setTestEnvironmentId(testEnviromentId);
		}
		TestToolMaster testToolMaster = new TestToolMaster();
		if(testEnviromentToolName!=null){
			testToolMaster.setTestToolName(testEnviromentToolName);
		}
		testEnviromentMaster.setTestToolMaster(testToolMaster);
		testRunConfigurationChild.setTestEnviromentMaster(testEnviromentMaster);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(!(testRunScheduledStartTime==null || testRunScheduledStartTime.trim() == ""))
				testRunConfigurationChild.setTestRunScheduledStartTime(sdf.parse(testRunScheduledStartTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		testRunConfigurationChild.setTestRunScheduledIntervalInHour(testRunScheduledIntervalInHour);
		testRunConfigurationChild.setTestRunRecurrenceLimit(testRunRecurrenceLimit);
		testRunConfigurationChild.setLastRunNo(lastRunNo);
		
		TestCategoryMaster testCategoryMaster = new TestCategoryMaster();
		if(testCategory!=null){
			testCategoryMaster.setTestCategory(testCategory);
		}
		testRunConfigurationChild.setTestCategoryMaster(testCategoryMaster);
		
		testRunConfigurationChild.setTestRunCronSchedule(testRunCronSchedule);
		testRunConfigurationChild.setDescription(description);
		testRunConfigurationChild.setTestRunConfigurationName(testRunConfigurationName);
		try {
			if(!(testRunScheduledEndTime==null || testRunScheduledEndTime.trim() == ""))
				testRunConfigurationChild.setTestRunScheduledEndTime(sdf.parse(testRunScheduledEndTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		testRunConfigurationChild.setNotifyByMail(notifyByMail);
		testRunConfigurationChild.setLocale(locale);
		
		return testRunConfigurationChild;		
	}
}
