/**
 * 
 */
package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestCaseEntityGroup;
import com.hcl.atf.taf.model.TestcaseTypeMaster;
import com.hcl.atf.taf.model.UserList;

/**
 * @author silambarasur
 *
 */
public class JsonTestCaseEntityGroup {

	@JsonProperty
	private Integer testCaseEntityGroupId;
	@JsonProperty
	private String testCaseEntityGroupName;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer testCaseTypeId;
	@JsonProperty
	private String testCaseTypeName;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer createdBy;
	@JsonProperty
	private Integer modifiedBy;
	
	@JsonProperty
	private Integer parentEntityGroupId;
	
	@JsonProperty
	private String parentEntityGroupName;
	
	@JsonProperty
	private Integer productId;
	
	@JsonProperty
	private String productName;
	
	@JsonProperty
	private String 	modifiedFieldTitle;
	
	public JsonTestCaseEntityGroup() {
		
	}
	
	
	public JsonTestCaseEntityGroup(TestCaseEntityGroup testCaseEntityGroups) {
		
		if(testCaseEntityGroups !=null && testCaseEntityGroups.getTestCaseEntityGroupId() != null) {
			this.testCaseEntityGroupId = testCaseEntityGroups.getTestCaseEntityGroupId();
		}
		
		this.testCaseEntityGroupName = testCaseEntityGroups.getTestCaseEntityGroupName();
		this.description = testCaseEntityGroups.getDescription();
		
		if(testCaseEntityGroups.getCreatedBy() != null){
			this.createdBy = testCaseEntityGroups.getCreatedBy().getUserId();
		}
		
		if(testCaseEntityGroups.getModifiedBy() != null){
			this.modifiedBy = testCaseEntityGroups.getModifiedBy().getUserId();
		}
		
		if(testCaseEntityGroups.getModifiedDate() != null){
			this.createdDate = DateUtility.dateToStringWithSeconds1(testCaseEntityGroups.getCreatedDate());
		}
		
		if(testCaseEntityGroups.getModifiedDate() != null){
			this.modifiedDate = DateUtility.dateToStringWithSeconds1(testCaseEntityGroups.getModifiedDate());
		}
		 if(testCaseEntityGroups.getParentEntityGroupId() != null){
			 
			 this.parentEntityGroupId= testCaseEntityGroups.getParentEntityGroupId().getTestCaseEntityGroupId();
			 this.parentEntityGroupName= testCaseEntityGroups.getParentEntityGroupId().getTestCaseEntityGroupName();
		 }
		 if(testCaseEntityGroups.getProduct() != null) {
			 this.productId = testCaseEntityGroups.getProduct().getProductId();
			 this.productName = testCaseEntityGroups.getProduct().getProductName();
		 }
		 if(testCaseEntityGroups.getType() != null){
			 this.testCaseTypeId = testCaseEntityGroups.getType().getTestcaseTypeId();
			 this.testCaseTypeName = testCaseEntityGroups.getType().getName();
		 }
	}
	
	@JsonIgnore
	public TestCaseEntityGroup getTestCaseEntityGroup() {
		TestCaseEntityGroup testCaseEntityGroup = new TestCaseEntityGroup();
		
		if( this.testCaseEntityGroupId != null){
			testCaseEntityGroup.setTestCaseEntityGroupId(testCaseEntityGroupId);
		}
		testCaseEntityGroup.setTestCaseEntityGroupName(testCaseEntityGroupName);
		testCaseEntityGroup.setDescription(description);
		
		if(this.modifiedBy != null){
			UserList userModifiedBy = new UserList();
			userModifiedBy.setUserId(modifiedBy);					
			testCaseEntityGroup.setModifiedBy(userModifiedBy);
		}
		
		if(this.createdBy != null){
			UserList userCreation = new UserList();
			userCreation.setUserId(createdBy);						
			testCaseEntityGroup.setCreatedBy(userCreation);
		}
		
		
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			testCaseEntityGroup.setCreatedDate(DateUtility.getCurrentTime());
		} else {
		
			testCaseEntityGroup.setCreatedDate(DateUtility.dateformatWithOutTime(createdDate));
		}
		
		if(this.modifiedDate == null || this.modifiedDate.trim().isEmpty()) {
			testCaseEntityGroup.setModifiedDate(DateUtility.getCurrentTime());
		} else {
		
			testCaseEntityGroup.setModifiedDate(DateUtility.dateformatWithOutTime(this.modifiedDate));
		}
		if((this.parentEntityGroupId !=null)) {
			TestCaseEntityGroup parentEntityGroup = new TestCaseEntityGroup();
			parentEntityGroup.setTestCaseEntityGroupId(this.parentEntityGroupId);
			testCaseEntityGroup.setParentEntityGroupId(parentEntityGroup);
		}
		
		if(this.productId != null ) {
			ProductMaster product= new ProductMaster();
			product.setProductId(productId);
			testCaseEntityGroup.setProduct(product);
		}
		
		if(this.testCaseTypeId != null){
			TestcaseTypeMaster testcaseTypeMaster = new TestcaseTypeMaster();
			testcaseTypeMaster.setTestcaseTypeId(testCaseTypeId);
			testCaseEntityGroup.setType(testcaseTypeMaster);
		}
		return testCaseEntityGroup;
	}
	
	public Integer getTestCaseEntityGroupId() {
		return testCaseEntityGroupId;
	}
	public String getTestCaseEntityGroupName() {
		return testCaseEntityGroupName;
	}
	public String getDescription() {
		return description;
	}
	public Integer getTestCaseTypeId() {
		return testCaseTypeId;
	}
	public String getTestCaseTypeName() {
		return testCaseTypeName;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public Integer getModifiedBy() {
		return modifiedBy;
	}
	public void setTestCaseEntityGroupId(Integer testCaseEntityGroupId) {
		this.testCaseEntityGroupId = testCaseEntityGroupId;
	}
	public void setTestCaseEntityGroupName(String testCaseEntityGroupName) {
		this.testCaseEntityGroupName = testCaseEntityGroupName;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setTestCaseTypeId(Integer testCaseTypeId) {
		this.testCaseTypeId = testCaseTypeId;
	}
	public void setTestCaseTypeName(String testCaseTypeName) {
		this.testCaseTypeName = testCaseTypeName;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


	public Integer getParentEntityGroupId() {
		return parentEntityGroupId;
	}


	public void setParentEntityGroupId(Integer parentEntityGroupId) {
		this.parentEntityGroupId = parentEntityGroupId;
	}


	public String getParentEntityGroupName() {
		return parentEntityGroupName;
	}


	public void setParentEntityGroupName(String parentEntityGroupName) {
		this.parentEntityGroupName = parentEntityGroupName;
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


	public String getModifiedFieldTitle() {
		return modifiedFieldTitle;
	}


	public void setModifiedFieldTitle(String modifiedFieldTitle) {
		this.modifiedFieldTitle = modifiedFieldTitle;
	}
	

}
