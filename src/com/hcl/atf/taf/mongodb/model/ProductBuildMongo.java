package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ProductBuild;

@Document(collection = "productbuilds")
public class ProductBuildMongo {
	@Id
	private String id;
	private Integer buildId;
	private String buildname;
	private String buildDescription;
	private Date buildDate;
	private Integer versionId;
	private String versionName;
	private Integer productId;
	private String productName;
	private Integer testFactoryId;
	private String testFactoryName;
	private Integer testCentersId;
	private String testCentersName;
	
	private Integer customerId;
	private String customerName;
	private String build_type;//ISE
	private String wt_distribution;//ISE
	private String updated_by;	//ISE
	private String buildNo;
	private String status;
	
	private String parentStatus;	
	
	private Date createdDate;//ISE
	private Date modifiedDate;//ISE
	
	public ProductBuildMongo(){
		
	}
	
	public ProductBuildMongo(Integer buildId, Integer versionId, String build_type, String wt_distribution, Date createdDate, 
			Date modifiedDate, String updated_by, String buildNo, String buildname, String buildDescription,
		Date buildDate, String status, Integer productVersionId){
		this.buildId = buildId;
		this.versionId = versionId;
		this.build_type = build_type;
		this.wt_distribution = wt_distribution;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.updated_by = updated_by;
		this.buildNo = buildNo;
		this.buildname = buildname;
		this.buildDescription = buildDescription;
		this.buildDate = buildDate;
		this.status = status;
		this. productName=productName;
		this. productId=productId;
		this. customerId=customerId;
		this.customerName=customerName;
		this. testFactoryId=testFactoryId;
		this. testFactoryName=testFactoryName;
		
	}	
	
public ProductBuildMongo(ProductBuild productBuild) {
		this.id= productBuild.getProductBuildId()+"";
		this.buildId = productBuild.getProductBuildId();
		if(productBuild.getProductVersion() != null){
			this.versionId = productBuild.getProductVersion().getProductVersionListId();
			this.versionName = productBuild.getProductVersion().getProductVersionName();
			if(productBuild.getProductVersion().getStatus() != null && productBuild.getProductVersion().getStatus() == 1){
				this.parentStatus="Active";
			}else{
				this.parentStatus="InActive";
			}
			
		}
		
		this.build_type = productBuild.getBuildType().getStageName();
		this.createdDate = productBuild.getCreatedDate();
		this.modifiedDate = productBuild.getModifiedDate();
		
		this.buildNo = productBuild.getBuildNo();
		this.buildname = productBuild.getBuildname();
		this.buildDescription = productBuild.getBuildDescription();
		this.buildDate = productBuild.getBuildDate();
		
		if (productBuild.getStatus() == 1){
			this.status = "Active";
		}else{
			this.status = "InActive";
		}
		
		this.productId=productBuild.getProductVersion().getProductMaster().getProductId();
		this.productName=productBuild.getProductVersion().getProductMaster().getProductName();
		this.customerId=productBuild.getProductVersion().getProductMaster().getCustomer().getCustomerId();
		this.customerName = productBuild.getProductVersion().getProductMaster().getCustomer().getCustomerName();
		this.testFactoryId=productBuild.getProductVersion().getProductMaster().getTestFactory().getTestFactoryId();
		this.testFactoryName=productBuild.getProductVersion().getProductMaster().getTestFactory().getTestFactoryName();
		this.testCentersId=productBuild.getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
		this.testCentersName=productBuild.getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
		
		
	}
	
	@Override
    public String toString(){
	return buildId+":"+versionId+":"+buildname+":"+buildDescription+":"+status+":"+build_type+":"+ wt_distribution+":"+
			createdDate+":"+modifiedDate+":"+updated_by+":"+buildNo+":"+buildDate+":"+status+":";
    }

	
	

	public String getBuild_type() {
		return build_type;
	}

	public void setBuild_type(String build_type) {
		this.build_type = build_type;
	}

	public String getWt_distribution() {
		return wt_distribution;
	}

	public void setWt_distribution(String wt_distribution) {
		this.wt_distribution = wt_distribution;
	}

	

	

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	public String getBuildNo() {
		return buildNo;
	}

	public void setBuildNo(String buildNo) {
		this.buildNo = buildNo;
	}

	public String getBuildname() {
		return buildname;
	}

	public void setBuildname(String buildname) {
		this.buildname = buildname;
	}

	public String getBuildDescription() {
		return buildDescription;
	}

	public void setBuildDescription(String buildDescription) {
		this.buildDescription = buildDescription;
	}

	public Date getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(Date buildDate) {
		this.buildDate = buildDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Integer getBuildId() {
		return buildId;
	}

	public void setBuildId(Integer buildId) {
		this.buildId = buildId;
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

	

	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
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

	public Integer getTestCentersId() {
		return testCentersId;
	}

	public void setTestCentersId(Integer testCentersId) {
		this.testCentersId = testCentersId;
	}

	
	
}
