package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.TestCaseStepsList;

@Document(collection = "testSteps")
public class TestCaseStepsListMongo {
	
	@Id
	private String id;
	private Integer testStepId;
	private String testStepName;
	private String testStepDescription;
	private String testStepInput;
	private String testStepExpectedOutput;
	private Integer testCaseId;
	private String testCaseName;
	private String testStepCode;
	private String testStepSource;
	
	private Integer productId;
	private String productName;
	private Integer testFactoryId;
	private String testFactoryName;
	
	private Integer testCentersId;
	private String testCentersName;
	
	private Integer customerId;
	private String customerName;
	
	private Date createdDate;
	private Date modifiedDate;
	
	


		
	
	public TestCaseStepsListMongo(){
		
	}
	
	

	public TestCaseStepsListMongo(TestCaseStepsList testCaseStep) {
		
		if(testCaseStep!=null){
			this. id=testCaseStep.getTestStepId()+"";
			this.testStepId=testCaseStep.getTestStepId();
			this. testStepName=testCaseStep.getTestStepName();
			this. testStepDescription=testCaseStep.getTestStepDescription();
			this. testStepInput=testCaseStep.getTestStepInput();
			this. testStepExpectedOutput=testCaseStep.getTestStepExpectedOutput();
			this.testStepCode=testCaseStep.getTestStepCode();
			this.testStepSource=testCaseStep.getTestStepSource();
			this.createdDate=testCaseStep.getTestStepCreatedDate();
			this.modifiedDate=testCaseStep.getTestStepLastUpdatedDate();
			
			if(testCaseStep.getTestCaseList()!=null){
				this.testCaseId=testCaseStep.getTestCaseList().getTestCaseId();
				this.testCaseName=testCaseStep.getTestCaseList().getTestCaseName();
				if(testCaseStep.getTestCaseList().getProductMaster()!=null){
					this.productId=testCaseStep.getTestCaseList().getProductMaster().getProductId();
					this.productName=testCaseStep.getTestCaseList().getProductMaster().getProductName();
					if(testCaseStep.getTestCaseList().getProductMaster().getTestFactory()!=null){
						this.testFactoryId=testCaseStep.getTestCaseList().getProductMaster().getTestFactory().getTestFactoryId();
						this.testFactoryName=testCaseStep.getTestCaseList().getProductMaster().getTestFactory().getTestFactoryName();
						if(testCaseStep.getTestCaseList().getProductMaster().getTestFactory().getTestFactoryLab()!=null){
							this.testCentersId=testCaseStep.getTestCaseList().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
							this.testCentersName=testCaseStep.getTestCaseList().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
						}
					}
				}
			}
		}
		this.customerId=testCaseStep.getTestCaseList().getProductMaster().getCustomer().getCustomerId();
		this.customerName=testCaseStep.getTestCaseList().getProductMaster().getCustomer().getCustomerName();
		
	}



	public String getId() {
		return id;
	}



	public void setId(String _id) {
		this.id = _id;
	}



	public Integer getTestStepId() {
		return testStepId;
	}



	public void setTestStepId(Integer testStepId) {
		this.testStepId = testStepId;
	}



	public String getTestStepName() {
		return testStepName;
	}



	public void setTestStepName(String testStepName) {
		this.testStepName = testStepName;
	}



	public String getTestStepDescription() {
		return testStepDescription;
	}



	public void setTestStepDescription(String testStepDescription) {
		this.testStepDescription = testStepDescription;
	}



	public String getTestStepInput() {
		return testStepInput;
	}



	public void setTestStepInput(String testStepInput) {
		this.testStepInput = testStepInput;
	}



	public String getTestStepExpectedOutput() {
		return testStepExpectedOutput;
	}



	public void setTestStepExpectedOutput(String testStepExpectedOutput) {
		this.testStepExpectedOutput = testStepExpectedOutput;
	}



	public Integer getTestCaseId() {
		return testCaseId;
	}



	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}



	public String getTestStepCode() {
		return testStepCode;
	}



	public void setTestStepCode(String testStepCode) {
		this.testStepCode = testStepCode;
	}



	public String getTestStepSource() {
		return testStepSource;
	}



	public void setTestStepSource(String testStepSource) {
		this.testStepSource = testStepSource;
	}



	public Date getCreatedDate() {
		return createdDate;
	}



	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}



	public Date getModifiedDate() {
		return modifiedDate;
	}



	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
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



	Integer getTestCentersId() {
		return testCentersId;
	}



	void setTestCentersId(Integer testCentersId) {
		this.testCentersId = testCentersId;
	}



	String getTestCentersName() {
		return testCentersName;
	}



	void setTestCentersName(String testCentersName) {
		this.testCentersName = testCentersName;
	}



	public String getTestCaseName() {
		return testCaseName;
	}



	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	
	
}
