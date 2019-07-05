package com.hcl.atf.taf.model.json;

import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductMode;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.TestFactory;


public class JsonProductMaster implements java.io.Serializable {
	private static final Log log = LogFactory
			.getLog(JsonProductMaster.class);
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private String projectCode;
	@JsonProperty
	private String projectName;
	@JsonProperty
	private String productDescription;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private Integer testFactoryId;
	@JsonProperty
	private String testFactoryName;
	@JsonProperty
	private Integer customerId;
	@JsonProperty
	private String customerName;
	@JsonProperty
	private String customerDescription;
	@JsonProperty
	private String customerRefId;
	@JsonProperty
	private Integer customerStatus;
	@JsonProperty
	private Integer productTypeId;
	@JsonProperty
	private String typeName;
	@JsonProperty
	private Integer modeId;
	@JsonProperty
	private String modeName;

	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String statusChangeDate;
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String 	modifiedFieldTitle;	
	@JsonProperty
	private String oldFieldID;
	@JsonProperty
	private String	oldFieldValue;
	@JsonProperty
	private String	modifiedFieldID;
	@JsonProperty	
	private String modifiedFieldValue;
	
	
	public Integer getTestFactoryId() {
		return testFactoryId;
	}

	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}

	public JsonProductMaster() {
	}

	public JsonProductMaster(ProductMaster productmaster) {
		this.productName = productmaster.getProductName();
		this.projectCode = productmaster.getProjectCode();
		this.projectName = productmaster.getProjectName();	
		
		
		if(productmaster.getCreatedDate()!=null){
			log.debug("productmaster.getCreatedDate()***"+DateUtility.dateToStringWithSeconds1(productmaster.getCreatedDate()));
			this.createdDate = DateUtility.dateToStringWithSeconds1(productmaster.getCreatedDate());
		}
		if(productmaster.getStatusChangeDate()!=null){
			this.statusChangeDate = DateUtility.dateformatWithOutTime(productmaster.getStatusChangeDate());
		}
		
		if(productmaster.getStatus() != null){
			this.status = productmaster.getStatus();
		}
		if(productmaster.getProductMode()!=null){
			modeId = productmaster.getProductMode().getModeId();
			modeName = productmaster.getProductMode().getModeName();
		}
		
		this.productId=productmaster.getProductId();
		this.productDescription=productmaster.getProductDescription();
		if(productmaster.getTestFactory()!=null){
			this.testFactoryId=productmaster.getTestFactory().getTestFactoryId();
			this.testFactoryName=productmaster.getTestFactory().getTestFactoryName();
		}
		if(productmaster.getCustomer() != null) {
			this.customerId = productmaster.getCustomer().getCustomerId();
			this.customerName = productmaster.getCustomer().getCustomerName();
		}
		if(productmaster.getCustomer() != null){
			this.customerId = productmaster.getCustomer().getCustomerId();
			this.customerName = productmaster.getCustomer().getCustomerName();
			this.customerDescription = productmaster.getCustomer().getDescription();
			this.customerRefId = productmaster.getCustomer().getCustomerRefId();
			this.customerStatus = productmaster.getCustomer().getStatus();
		}else{
			this.customerId =0;
			this.customerName = null;
			this.customerDescription = null;
			this.customerRefId = null;
			this.customerStatus = 0;
		}
		
		if(productmaster.getProductType()!=null){
			this.productTypeId=productmaster.getProductType().getProductTypeId();
			this.typeName=productmaster.getProductType().getTypeName();
		}
	}


	@JsonProperty
	public Integer getProductId() {
		return this.productId;
	}

	@JsonProperty
	public void setProductId(Integer productId) {
		this.productId = productId;		
	}

	@JsonProperty
	public String getProductName() {
		return this.productName;
	}

	@JsonProperty
	public void setProductName(String productName) {
		this.productName = productName;
	}


	@JsonProperty
	public String getProductDescription() {
		return this.productDescription;
	}
	@JsonProperty
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	
	@JsonProperty
	public Integer getStatus() {
		return status;
	}

	@JsonProperty
	public void setStatus(Integer status) {
		
		this.status = status;
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
	@JsonIgnore
	public ProductMaster getProductMaster(){
		ProductMaster productMaster = new ProductMaster();
		
		if(productId!=null){
			productMaster.setProductId(productId);
		}
		productMaster.setProductName(productName);
		productMaster.setProjectCode(projectCode);
		productMaster.setProjectName(projectName);
		productMaster.setProductDescription(productDescription);
	
		log.debug("created*******"+this.createdDate);
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			productMaster.setCreatedDate(DateUtility.getCurrentTime());
		} else {
			productMaster.setCreatedDate(DateUtility.toFormatDate(this.createdDate));
		}
		productMaster.setStatusChangeDate(DateUtility.getCurrentTime());
		
		
		
		if(this.status != null ){			
			productMaster.setStatus(status);			
		}else{
			productMaster.setStatus(0);	
		}
		if(this.modeId != null ){
			ProductMode pMode = new ProductMode();
			pMode.setModeId(modeId);
			pMode.setModeName(modeName);
			productMaster.setProductMode(pMode);
		}
		
		  if(this.testFactoryId!=null){
				TestFactory tFactory = new TestFactory();
				tFactory.setTestFactoryId(testFactoryId);
				tFactory.setTestFactoryName(testFactoryName);
				productMaster.setTestFactory(tFactory);
			}		
		
		  Customer customer = new Customer();
		  customer.setCustomerId(customerId);
		  productMaster.setCustomer(customer);// Product object is passed	// not ID.		 
		ProductType productType= new ProductType();
		productType.setProductTypeId(productTypeId);
		productMaster.setProductType(productType);
		  
		return productMaster;
	}
	@JsonIgnore
	public JSONObject getCleanJson() {
		
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put("productName", productName);
			responseJson.put("productId", productId);
			responseJson.put("testFactoryName", testFactoryName);
			responseJson.put("testFactoryId", testFactoryId);
			responseJson.put("customerName", customerName);
			responseJson.put("customerId", customerId);
			responseJson.put("createdDate", createdDate);
			responseJson.put("typeName", typeName);
			
			
			responseJson.put("status", status);
		}catch(Exception e) {
			
		}
		return responseJson;
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

	public String getCustomerDescription() {
		return customerDescription;
	}

	public void setCustomerDescription(String customerDescription) {
		this.customerDescription = customerDescription;
	}

	public String getCustomerRefId() {
		return customerRefId;
	}

	public void setCustomerRefId(String customerRefId) {
		this.customerRefId = customerRefId;
	}

	public Integer getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(Integer customerStatus) {
		this.customerStatus = customerStatus;
	}

	public static Comparator<JsonProductMaster> jsonProductMasterComparator = new Comparator<JsonProductMaster>() {

		public int compare(JsonProductMaster productid1, JsonProductMaster productid2) {
			Integer product1Id = productid1.getProductId();
			Integer product2Id = productid2.getProductId();
			return product1Id.compareTo(product2Id);
		}
	};

	public Integer getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Integer productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getModeId() {
		return modeId;
	}

	public void setModeId(Integer modeId) {
		this.modeId = modeId;
	}

	public String getModeName() {
		return modeName;
	}

	public void setModeName(String modeName) {
		this.modeName = modeName;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getStatusChangeDate() {
		return statusChangeDate;
	}

	public void setStatusChangeDate(String statusChangeDate) {
		this.statusChangeDate = statusChangeDate;
	}

	public String getModifiedField() {
		return modifiedField;
	}

	public void setModifiedField(String modifiedField) {
		this.modifiedField = modifiedField;
	}

	public String getModifiedFieldTitle() {
		return modifiedFieldTitle;
	}

	public void setModifiedFieldTitle(String modifiedFieldTitle) {
		this.modifiedFieldTitle = modifiedFieldTitle;
	}

	public String getOldFieldID() {
		return oldFieldID;
	}

	public void setOldFieldID(String oldFieldID) {
		this.oldFieldID = oldFieldID;
	}

	public String getOldFieldValue() {
		return oldFieldValue;
	}

	public void setOldFieldValue(String oldFieldValue) {
		this.oldFieldValue = oldFieldValue;
	}

	public String getModifiedFieldID() {
		return modifiedFieldID;
	}

	public void setModifiedFieldID(String modifiedFieldID) {
		this.modifiedFieldID = modifiedFieldID;
	}

	public String getModifiedFieldValue() {
		return modifiedFieldValue;
	}

	public void setModifiedFieldValue(String modifiedFieldValue) {
		this.modifiedFieldValue = modifiedFieldValue;
	}
	
}
