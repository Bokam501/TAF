package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ProductMaster;


@Document(collection = "products")
public class ProductMasterMongo {
	
	@Id
	private String id;
	private Integer productId;
	private String productName;
	private String productDescription;
	private Integer testFactoryId;
	private String testFactoryName;
	private String projectCode;
	private String projectName;
	private String typeName;
	private String status;
	private Integer customerId;
	private String customerName;
	private Integer modeId;
	private String modeName;
	private Integer testCentersId;
	private String testCentersName;
	
	private String parentStatus;
	
	private Date createdDate;
	private Date modifiedDate;

	//private ProductType productType;
/*	private Integer productTypeId;
	private Integer shiftAttendanceGraceTime;
	private Integer weeklyOverTimeLimit;
	private Integer shiftLunchAuthorisedTime;
	private Integer shiftBreaksAuthorisedTime;*/
	
	
	

	public ProductMasterMongo(){
		
	}
	
	public ProductMasterMongo(	String testFactoryName,String customerName,Integer productId, String productName,String productDescription,String
		    projectCode,String projectName,String typeName,String modeName,String status, 
		      Integer testFactoryId, Integer customerId, Integer modeId	){
		
		this.testFactoryName=testFactoryName;
		this.customerName=customerName;
		this.productId = productId;
		this.productName=productName;
		this.productDescription=productDescription;
		this.projectCode=projectCode;
		this.projectName=projectName;
		this.typeName=typeName;
		this.modeName=modeName;
		this.status=status;
		this.modifiedDate = modifiedDate;
		this.createdDate = createdDate;
		this.testFactoryId = testFactoryId;
		this.customerId = customerId;
		this.modeId = modeId;		
		
	}
	
public ProductMasterMongo(ProductMaster product) {
		this.id = product.getProductId()+"";
		if(product.getTestFactory()!=null){
			this.testFactoryName = product.getTestFactory().getTestFactoryName();
			this.testFactoryId=product.getTestFactory().getTestFactoryId();
			if(product.getTestFactory().getStatus() == 1){
				this.parentStatus = "Active";
			}else{
				this.parentStatus = "InActive";
			}
			
		}
		this.customerName = product.getCustomer().getCustomerName();
		this.productName=product.getProductName();
		this.productDescription=product.getProductDescription();
		this.projectCode=product.getProjectCode();
		this.projectName=product.getProjectName();
		this.typeName=product.getProductType().getTypeName();
		this.modeName=product.getProductMode().getModeName();
		
		if (product.getStatus() == 1){
			this.status = "Active";
		}else{
			this.status = "InActive";
		}
			
		this.productId=product.getProductId();
		this.modifiedDate=product.getStatusChangeDate();
		this.createdDate=product.getCreatedDate();
		
		this.customerId=product.getCustomer().getCustomerId();
		this.modeId=product.getProductMode().getModeId();
		if(product.getTestFactory()!=null && product.getTestFactory().getTestFactoryLab()!=null ){
			this.testCentersId=product.getTestFactory().getTestFactoryLab().getTestFactoryLabId();
			this.testCentersName=product.getTestFactory().getTestFactoryLab().getTestFactoryLabName();
		}
	}
	
	
	public String getTestFactoryName() {
		return testFactoryName;
	}
	public void setTestFactoryName(String testFactoryName) {
		this.testFactoryName = testFactoryName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getModeName() {
		return modeName;
	}
	public void setModeName(String modeName) {
		this.modeName = modeName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	 public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getTestFactoryId() {
		return testFactoryId;
	}

	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getModeId() {
		return modeId;
	}

	public void setModeId(Integer modeId) {
		this.modeId = modeId;
	}

	/*public Integer getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Integer productTypeId) {
		this.productTypeId = productTypeId;
	}

	public Integer getShiftAttendanceGraceTime() {
		return shiftAttendanceGraceTime;
	}

	public void setShiftAttendanceGraceTime(Integer shiftAttendanceGraceTime) {
		this.shiftAttendanceGraceTime = shiftAttendanceGraceTime;
	}

	public Integer getWeeklyOverTimeLimit() {
		return weeklyOverTimeLimit;
	}

	public void setWeeklyOverTimeLimit(Integer weeklyOverTimeLimit) {
		this.weeklyOverTimeLimit = weeklyOverTimeLimit;
	}

	public Integer getShiftLunchAuthorisedTime() {
		return shiftLunchAuthorisedTime;
	}

	public void setShiftLunchAuthorisedTime(Integer shiftLunchAuthorisedTime) {
		this.shiftLunchAuthorisedTime = shiftLunchAuthorisedTime;
	}

	public Integer getShiftBreaksAuthorisedTime() {
		return shiftBreaksAuthorisedTime;
	}

	public void setShiftBreaksAuthorisedTime(Integer shiftBreaksAuthorisedTime) {
		this.shiftBreaksAuthorisedTime = shiftBreaksAuthorisedTime;
	}*/

	@Override
    public String toString(){
	return id+":"+testFactoryName+":"+customerName+":"+productName+":"+ productDescription+":"+
    projectCode+":"+projectName+":"+typeName+":"+modeName+":"+status;
    }

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
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

	public String getParentStatus() {
		return parentStatus;
	}

	public void setParentStatus(String parentStatus) {
		this.parentStatus = parentStatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
	}

	
	
}
