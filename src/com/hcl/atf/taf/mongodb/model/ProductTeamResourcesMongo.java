package com.hcl.atf.taf.mongodb.model;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ProductTeamResources;


@Document(collection = "productsteamresources")
public class ProductTeamResourcesMongo {
	private static final Log log = LogFactory.getLog(ProductTeamResourcesMongo.class);
	@Transient
	private SimpleDateFormat dateFormatForMongoDB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
	
	@Id
	private String id;
	private String _class;
	private Integer productTeamResourceId;
	private Object fromDate;
	private Object toDate;
	private String remarks;
	private Integer userId;
	private String userName;
	
	private Integer productSpecificUserRoleId;
	private String productSpecificUserRoleName;
	
	private Integer dimensionMasterId;
	private String dimensionMasterName;
	
	private Integer productId;
	private String productName;
	private Integer testFactoryId;
	private String testFactoryName;
	private Integer testCentersId;
	private String testCentersName;
	
	private String status;
	private Integer customerId;
	private String customerName;
	private String parentStatus;
	private Object createdDate;
	private Object modifiedDate;



	public ProductTeamResourcesMongo(){

	}



	public ProductTeamResourcesMongo(ProductTeamResources productTeamResource) {

		this.id = productTeamResource.getProductTeamResourceId()+"";
		this._class="";
		this.productTeamResourceId=productTeamResource.getProductTeamResourceId();
		this.fromDate=productTeamResource.getFromDate();
		this.toDate=productTeamResource.getToDate();
		this.remarks=productTeamResource.getRemarks();
		
		if(productTeamResource.getProductSpecificUserRole()!=null){
			this.productSpecificUserRoleId=productTeamResource.getProductSpecificUserRole().getUserRoleId();
			this.productSpecificUserRoleName=productTeamResource.getProductSpecificUserRole().getRoleName();
		}
		
		
		if(productTeamResource.getUserList()!=null){
			this.userId=productTeamResource.getUserList().getUserId();
			this.userName=productTeamResource.getUserList().getLoginId();
			
		}
		if(productTeamResource.getDimensionMaster()!=null){
			this.dimensionMasterId=productTeamResource.getDimensionMaster().getDimensionId();
			this.dimensionMasterName=productTeamResource.getDimensionMaster().getName();
		}
		
		if(productTeamResource.getProductMaster()!=null){
			this.productId=productTeamResource.getProductMaster().getProductId();
			this.productName=productTeamResource.getProductMaster().getProductName();
			if(productTeamResource.getProductMaster().getStatus() != null && productTeamResource.getProductMaster().getStatus() == 1){
				this.parentStatus = "Active";
			}else{
				this.parentStatus = "InActive";
			}
			

		}

		if(productTeamResource.getProductMaster()!=null && productTeamResource.getProductMaster().getTestFactory()!=null){
			this.testFactoryId=productTeamResource.getProductMaster().getTestFactory().getTestFactoryId();
			this.testFactoryName=productTeamResource.getProductMaster().getTestFactory().getTestFactoryName();
			
			if(productTeamResource.getProductMaster()!=null && productTeamResource.getProductMaster().getTestFactory().getTestFactoryLab()!=null){
				this.testCentersId=productTeamResource.getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
				this.testCentersName=productTeamResource.getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
				
			}

		}


		if(productTeamResource.getProductMaster()!=null && productTeamResource.getProductMaster().getCustomer()!=null){
			this.customerId=productTeamResource.getProductMaster().getCustomer().getCustomerId();
			this.customerName=productTeamResource.getProductMaster().getCustomer().getCustomerName();

		}

		this.modifiedDate=productTeamResource.getModifiedDate();
		this.createdDate=productTeamResource.getCreatedDate();
		if (productTeamResource.getStatus() == 1){
			this.status = "Active";
		}else{
			this.status = "InActive";
		}



	}



	public SimpleDateFormat getDateFormatForMongoDB() {
		return dateFormatForMongoDB;
	}



	public void setDateFormatForMongoDB(SimpleDateFormat dateFormatForMongoDB) {
		this.dateFormatForMongoDB = dateFormatForMongoDB;
	}

	

	public String getId() {
		return id;
	}



	public void setId(String _id) {
		this.id = _id;
	}



	public String get_class() {
		return _class;
	}



	public void set_class(String _class) {
		this._class = _class;
	}



	public Integer getProductTeamResourceId() {
		return productTeamResourceId;
	}



	public void setProductTeamResourceId(Integer productTeamResourceId) {
		this.productTeamResourceId = productTeamResourceId;
	}



	public Object getFromDate() {
		return fromDate;
	}



	public void setFromDate(Object fromDate) {

		String formatCheck = fromDate.toString();
		
		if(formatCheck.contains("date=")){
			try {
				fromDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.fromDate = fromDate;
	
	}



	public Object getToDate() {
		return toDate;
	}



	public void setToDate(Object toDate) {
		String formatCheck = toDate.toString();
		
		if(formatCheck.contains("date=")){
			try {
				toDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.toDate = toDate;
	
	}



	public String getRemarks() {
		return remarks;
	}



	public void setRemarks(String remarks) {
		this.remarks = remarks;
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



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
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



	public Integer getProductSpecificUserRoleId() {
		return productSpecificUserRoleId;
	}



	public void setProductSpecificUserRoleId(Integer productSpecificUserRoleId) {
		this.productSpecificUserRoleId = productSpecificUserRoleId;
	}



	public String getProductSpecificUserRoleName() {
		return productSpecificUserRoleName;
	}



	public void setProductSpecificUserRoleName(String productSpecificUserRoleName) {
		this.productSpecificUserRoleName = productSpecificUserRoleName;
	}



	public Integer getDimensionMasterId() {
		return dimensionMasterId;
	}



	public void setDimensionMasterId(Integer dimensionMasterId) {
		this.dimensionMasterId = dimensionMasterId;
	}



	public String getDimensionMasterName() {
		return dimensionMasterName;
	}



	public void setDimensionMasterName(String dimensionMasterName) {
		this.dimensionMasterName = dimensionMasterName;
	}



	public Integer getUserId() {
		return userId;
	}



	public void setUserId(Integer userId) {
		this.userId = userId;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
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


	


	

}
