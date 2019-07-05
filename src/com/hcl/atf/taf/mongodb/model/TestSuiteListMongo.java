package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.TestSuiteList;

@Document(collection = "testSuite")
public class TestSuiteListMongo {
	
	@Id
	private String id;
	private Integer testSuiteId;
	private String testSuiteScriptFileLocation;
	private String testSuiteName;
	private Integer scriptTypeMasterId;
	private String scriptTypeMasterName;
	
	private Integer productId;
	private String productName;
	private Integer versionId;
	private String versionName;
	
	private Integer testFactoryId;
	private String testFactoryName;
	private Integer testCentersId;
	private String testCentersName;
	private Integer customerId;
	private String customerName;
	
	private String status;
	private String testSuiteCode;
	private String testScriptSource;
	private Integer executionTypeMasterId;
	private String executionTypeMasterName;
	private Integer executionPriorityId;
	private String executionPriorityName;
	private String description;
	private Date modifiedDate;
	
	public TestSuiteListMongo(){
			
	}
	
	public TestSuiteListMongo(TestSuiteList testSuite) {
		
		if(testSuite!=null){
			this.id = testSuite.getTestSuiteId()+"";
			this.testSuiteId=testSuite.getTestSuiteId();
			this.testSuiteScriptFileLocation=testSuite.getTestSuiteScriptFileLocation();
			this.testSuiteName=testSuite.getTestSuiteName();
			
			if (testSuite.getStatus().equals("1"))
				this.status = "Active";
			else
				this.status = "InActive";
			
			
			this.description=testSuite.getDescription();
			this.modifiedDate=testSuite.getStatusChangeDate();
			this.testSuiteCode=testSuite.getTestSuiteCode();
			this.testScriptSource=testSuite.getTestScriptSource();
			
			if(testSuite.getProductMaster()!=null){
				this.productId=testSuite.getProductMaster().getProductId();
				this.productName=testSuite.getProductMaster().getProductName();
				if(testSuite.getProductVersionListMaster()!=null){
					this.versionId=testSuite.getProductVersionListMaster().getProductVersionListId();
					this.versionName=testSuite.getProductVersionListMaster().getProductVersionName();
				}
				
				if(testSuite.getProductMaster().getTestFactory()!=null){
					this.testFactoryId=testSuite.getProductMaster().getTestFactory().getTestFactoryId();
					this.testFactoryName=testSuite.getProductMaster().getTestFactory().getTestFactoryName();
					if(testSuite.getProductMaster().getTestFactory().getTestFactoryLab()!=null){
						this.testCentersId=testSuite.getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
						this.testCentersName=testSuite.getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
					}
				}
				
				if(testSuite.getProductMaster().getCustomer()!=null){
					this.customerId=testSuite.getProductMaster().getCustomer().getCustomerId();
					this.customerName=testSuite.getProductMaster().getCustomer().getCustomerName();
				}
			}
			if(testSuite.getExecutionTypeMaster()!=null){
				this.executionTypeMasterId=testSuite.getExecutionTypeMaster().getExecutionTypeId();
				this.executionTypeMasterName=testSuite.getExecutionTypeMaster().getName();
			}
			if(testSuite.getExecutionPriority()!=null){
				this.executionPriorityId=testSuite.getExecutionPriority().getTestcasePriorityId();
				this.executionPriorityName=testSuite.getExecutionPriority().getPriorityName();
			}
		}
		
	}

	

	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
	}

	public Integer getTestSuiteId() {
		return testSuiteId;
	}

	public void setTestSuiteId(Integer testSuiteId) {
		this.testSuiteId = testSuiteId;
	}

	public String getTestSuiteScriptFileLocation() {
		return testSuiteScriptFileLocation;
	}

	public void setTestSuiteScriptFileLocation(String testSuiteScriptFileLocation) {
		this.testSuiteScriptFileLocation = testSuiteScriptFileLocation;
	}

	public String getTestSuiteName() {
		return testSuiteName;
	}

	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}

	public Integer getScriptTypeMasterId() {
		return scriptTypeMasterId;
	}

	public void setScriptTypeMasterId(Integer scriptTypeMasterId) {
		this.scriptTypeMasterId = scriptTypeMasterId;
	}

	public String getScriptTypeMasterName() {
		return scriptTypeMasterName;
	}

	public void setScriptTypeMasterName(String scriptTypeMasterName) {
		this.scriptTypeMasterName = scriptTypeMasterName;
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

	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTestSuiteCode() {
		return testSuiteCode;
	}

	public void setTestSuiteCode(String testSuiteCode) {
		this.testSuiteCode = testSuiteCode;
	}

	public String getTestScriptSource() {
		return testScriptSource;
	}

	public void setTestScriptSource(String testScriptSource) {
		this.testScriptSource = testScriptSource;
	}

	public Integer getExecutionTypeMasterId() {
		return executionTypeMasterId;
	}

	public void setExecutionTypeMasterId(Integer executionTypeMasterId) {
		this.executionTypeMasterId = executionTypeMasterId;
	}

	public String getExecutionTypeMasterName() {
		return executionTypeMasterName;
	}

	public void setExecutionTypeMasterName(String executionTypeMasterName) {
		this.executionTypeMasterName = executionTypeMasterName;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


	

	

}
