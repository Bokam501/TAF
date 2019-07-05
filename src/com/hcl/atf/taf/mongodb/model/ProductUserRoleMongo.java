package com.hcl.atf.taf.mongodb.model;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ProductUserRole;


@Document(collection = "productuserspermission")
public class ProductUserRoleMongo {
	private static final Log log = LogFactory.getLog(ProductUserRoleMongo.class);
	@Transient
	private SimpleDateFormat dateFormatForMongoDB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
	
	@Id
	private Integer id;
	private String _class;
	private Integer productUserRoleId;
	private String name;
	private String description;
	private Integer userId;
	private String userName;
	private Integer roleId;
	private String roleName;
	private Integer productId;
	private String productName;
	private String productDescription;
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



	public ProductUserRoleMongo(){

	}



	public ProductUserRoleMongo(ProductUserRole productUserRole) {

		this.id = productUserRole.getProductUserRoleId();
		this._class="";

		this.productUserRoleId=productUserRole.getProductUserRoleId();
		this.name=productUserRole.getName();
		this.description=productUserRole.getDescription();

		if(productUserRole.getUser()!=null){
			this.userId=productUserRole.getUser().getUserId();
			this.userName=productUserRole.getUser().getLoginId();
		}

		if(productUserRole.getRole()!=null){
			this.roleId=productUserRole.getRole().getUserRoleId();
			this.roleName=productUserRole.getRole().getRoleName();

		}

		if(productUserRole.getProduct()!=null){
			this.productId=productUserRole.getProduct().getProductId();
			this.productName=productUserRole.getProduct().getProductName();

		}

		if(productUserRole.getProduct()!=null && productUserRole.getProduct().getTestFactory()!=null){
			this.testFactoryId=productUserRole.getProduct().getTestFactory().getTestFactoryId();
			this.testFactoryName=productUserRole.getProduct().getTestFactory().getTestFactoryName();
			
			this.testCentersId=productUserRole.getProduct().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
			this.testCentersName=productUserRole.getProduct().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
			

		}


		if(productUserRole.getProduct()!=null && productUserRole.getProduct().getCustomer()!=null){
			this.customerId=productUserRole.getProduct().getCustomer().getCustomerId();
			this.customerName=productUserRole.getProduct().getCustomer().getCustomerName();
			if(productUserRole.getProduct().getStatus() != null && productUserRole.getProduct().getStatus() == 1){
				this.parentStatus = "Active";
			}else{
				this.parentStatus = "InActive";
			}
			

		}

		this.modifiedDate=productUserRole.getModifiedDate();
		this.createdDate=productUserRole.getCreatedDate();
		if (productUserRole.getStatus() == 1){
			this.status = "Active";
		}else{
			this.status = "InActive";
		}



	}



	public Integer getId() {
		return id;
	}



	public void set_id(Integer _id) {
		this.id = _id;
	}



	public String get_class() {
		return _class;
	}



	public void set_class(String _class) {
		this._class = _class;
	}



	public Integer getProductUserRoleId() {
		return productUserRoleId;
	}



	public void setProductUserRoleId(Integer productUserRoleId) {
		this.productUserRoleId = productUserRoleId;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
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



	public Integer getRoleId() {
		return roleId;
	}



	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}



	public String getRoleName() {
		return roleName;
	}



	public void setRoleName(String roleName) {
		this.roleName = roleName;
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



	public String getProductDescription() {
		return productDescription;
	}



	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
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



	Integer getTestCentersId() {
		return testCentersId;
	}



	void setTestCentersId(Integer testCentersId) {
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
