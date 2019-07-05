package com.hcl.atf.taf.mongodb.model;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.TestCaseList;

@Document(collection = "testcases")
public class TestCaseMasterMongo  {
	
	
	private String id;
	private String _class;
	private String title;
    private String description;
    private String environment;
    private Object created_date;
    private Object last_updated_date;    
    private String primary_feature;
    private String primary_feature_parent;
    
    private String testcaseid;
	private String testCaseName;
	private String testCaseDescription;
	private String testCaseCode;
	private String status;
	private Integer productId;
	private String productName;
	private Integer testFactoryId;
	private String testFactoryName;
	private Integer testCentersId;
	private String testCentersName;
	
	private Integer customerId;
	private String customerName;
	private String testcaseType;
	private String testCaseSource;
	private Integer testcaseExecutionType;
	private String testcaseinput;
	private String testcaseexpectedoutput;
	private String preconditions;
	private Integer workflowStatusId;
	private String workflowStatusName;
	private Integer totalEffort;
	
	private String parentStatus;
	
	private Date createdDate;
	private Date modifiedDate;
	
	private String indexName;
	private String docType;
		
	public TestCaseMasterMongo() {
	
	
	}	

	public TestCaseMasterMongo(TestCaseList testCase) {
		this.id=testCase.getTestCaseId()+"";
		//this._id=testCase.getTestCaseName();
		this.indexName="testcases";
		this.docType="testcases";
		this.testcaseid=testCase.getTestCaseId().toString();
		
		this._class = this.getClass().getName().replace("\\", ".").replace("/", ".");
		this.title=testCase.getTestCaseName();
		this.description=testCase.getTestCaseDescription();
		this.created_date=testCase.getTestCaseCreatedDate();
		this.last_updated_date=testCase.getTestCaseLastUpdatedDate();
		
		
		if(testCase.getProductFeature()!=null && testCase.getProductFeature().size() >0){
			Set<ProductFeature> features = testCase.getProductFeature();
			if (features != null && !features.isEmpty()) {
				Iterator iter = features.iterator();
				ProductFeature feature = (ProductFeature) iter.next();
				this.primary_feature = feature.getProductFeatureName();
				this.primary_feature_parent = feature.getParentFeature() != null ?feature.getParentFeature().getProductFeatureName() : "N/A";  
			}
			
		}
		
		
		if(testCase.getStatus()!=null && testCase.getStatus() == 1){
			this.status="Active";
		}if(testCase.getStatus()!=null && testCase.getStatus() == 0){
			this.status="InActive";
		}
	
		if(testCase.getProductMaster()!=null){
			this.productId=testCase.getProductMaster().getProductId();
			this.productName=testCase.getProductMaster().getProductName();
			
			if(testCase.getProductMaster().getTestFactory()!=null){
				this.testFactoryId=testCase.getProductMaster().getTestFactory().getTestFactoryId();
				this.testFactoryName=testCase.getProductMaster().getTestFactory().getTestFactoryName();
				
				if(testCase.getProductMaster().getTestFactory().getTestFactoryLab()!=null){
					this.testCentersId=testCase.getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
					this.testCentersName=testCase.getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
				}
			}
			if(testCase.getProductMaster().getCustomer()!=null){
				this.customerId=testCase.getProductMaster().getCustomer().getCustomerId();
//				this.customerName=testCase.getProductMaster().getCustomer().getCustomerName();
			}
			
			
		}
		if(testCase.getWorkflowStatus()!=null){
			this.workflowStatusId = testCase.getWorkflowStatus().getWorkflowStatusId();
			this.workflowStatusName = testCase.getWorkflowStatus().getWorkflowStatusName();
		}
		this.totalEffort = testCase.getTotalEffort();
		
		if(testCase.getProductMaster()!=null){
			if(testCase.getProductMaster().getStatus() == 1 && testCase.getProductMaster().getStatus() != null){
				this.parentStatus = "Active";
			}else{
				this.parentStatus = "InActive";
			}
		}
		
		
		this.testCaseName=testCase.getTestCaseName();
		this.testCaseDescription=testCase.getTestCaseDescription();
		this.testCaseCode=testCase.getTestCaseCode();
		
		
		this.createdDate=testCase.getTestCaseCreatedDate();
		this.testcaseType=testCase.getTestCaseType();
		this.modifiedDate=testCase.getTestCaseLastUpdatedDate();
		
		this.testCaseSource=testCase.getTestCaseSource();
		this.testcaseExecutionType=testCase.getTestcaseExecutionType();
		this.testcaseinput=testCase.getPreconditions();
		this.testcaseexpectedoutput=testCase.getTestcaseexpectedoutput();
		this.preconditions=testCase.getPreconditions();
		
	}


	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
	}

	public String getTestcaseid() {
		return testcaseid;
	}

	public void setTestcaseid(String testcaseid) {
		this.testcaseid = testcaseid;
	}

	public String getTestCaseName() {
		return testCaseName;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}


	public String getTestCaseDescription() {
		return testCaseDescription;
	}

	public void setTestCaseDescription(String testCaseDescription) {
		this.testCaseDescription = testCaseDescription;
	}

	public String getTestCaseCode() {
		return testCaseCode;
	}

	public void setTestCaseCode(String testCaseCode) {
		this.testCaseCode = testCaseCode;
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


	public String getTestcaseType() {
		return testcaseType;
	}



	public void setTestcaseType(String testcaseType) {
		this.testcaseType = testcaseType;
	}




	public String getTestCaseSource() {
		return testCaseSource;
	}



	public void setTestCaseSource(String testCaseSource) {
		this.testCaseSource = testCaseSource;
	}



	public Integer getTestcaseExecutionType() {
		return testcaseExecutionType;
	}



	public void setTestcaseExecutionType(Integer testcaseExecutionType) {
		this.testcaseExecutionType = testcaseExecutionType;
	}



	public String getTestcaseinput() {
		return testcaseinput;
	}


	public void setTestcaseinput(String testcaseinput) {
		this.testcaseinput = testcaseinput;
	}


	public String getTestcaseexpectedoutput() {
		return testcaseexpectedoutput;
	}



	public void setTestcaseexpectedoutput(String testcaseexpectedoutput) {
		this.testcaseexpectedoutput = testcaseexpectedoutput;
	}



	public String getPreconditions() {
		return preconditions;
	}



	public void setPreconditions(String preconditions) {
		this.preconditions = preconditions;
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
	

	public Integer getWorkflowStatusId() {
		return workflowStatusId;
	}

	public void setWorkflowStatusId(Integer workflowStatusId) {
		this.workflowStatusId = workflowStatusId;
	}

	public String getWorkflowStatusName() {
		return workflowStatusName;
	}

	public void setWorkflowStatusName(String workflowStatusName) {
		this.workflowStatusName = workflowStatusName;
	}

	public Integer getTotalEffort() {
		return totalEffort;
	}
	public void setTotalEffort(Integer totalEffort) {
		this.totalEffort = totalEffort;
	}

	public String getParentStatus() {
		return parentStatus;
	}

	public void setParentStatus(String parentStatus) {
		this.parentStatus = parentStatus;
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

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public Object getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Object created_date) {
		this.created_date = created_date;
	}

	public Object getLast_updated_date() {
		return last_updated_date;
	}

	public void setLast_updated_date(Object last_updated_date) {
		this.last_updated_date = last_updated_date;
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

	public String get_class() {
		return _class;
	}

	public void set_class(String _class) {
		this._class = _class;
	}

	public String getIndexName() {
		return indexName;
	}

	public String getDocType() {
		return docType;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

}
